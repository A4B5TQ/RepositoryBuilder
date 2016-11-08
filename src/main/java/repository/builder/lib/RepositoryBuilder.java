package repository.builder.lib;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class RepositoryBuilder {

    private final static String SOURCE_CODE_PATH = "/src/main/java";
    private final static String REPOSITORY_DIRECTORY_NAME = "repositories";
    private final static String PROJECT_PATH = System.getProperty("user.dir");
    private final static String SOURCE_PATH = PROJECT_PATH + SOURCE_CODE_PATH;

    private final static String REPOSITORY_IMPORT = "import org.springframework.stereotype.Repository;";
    private final static String JPA_IMPORT = "import org.springframework.data.jpa.repository.JpaRepository;";
    private final static String REPOSITORY_ANNOTATION = "@Repository";
    private final static String REPOSITORY_INTERFACE_NAME = "public interface %s extends JpaRepository<%s,%s> {";
    private final static String CLOSE_BRACKET = "}";
    private final static String OPEN_BRACKET = "{";
    private final static String PACKAGE = "package ";
    private final static String REPOSITORY_NAME = "Repository";
    private static String MAIN_PATH = "";

    private static Map<String, Class> entityClasses = new HashMap<>();
    private final static Map<Class<?>, Class<?>> primitivesToWrapper = new HashMap<>();

    static {
        primitivesToWrapper.put(boolean.class, Boolean.class);
        primitivesToWrapper.put(byte.class, Byte.class);
        primitivesToWrapper.put(short.class, Short.class);
        primitivesToWrapper.put(char.class, Character.class);
        primitivesToWrapper.put(int.class, Integer.class);
        primitivesToWrapper.put(long.class, Long.class);
        primitivesToWrapper.put(float.class, Float.class);
        primitivesToWrapper.put(double.class, Double.class);
        entityClasses = getEntityFilesRecursive(new File(SOURCE_PATH), "");
    }

    private RepositoryBuilder() {

    }

    public static void build() {

        createJavaFile(entityClasses, "");
    }

    public static void build(String repositoryPostfix) {
        createJavaFile(entityClasses, repositoryPostfix);
    }

    private static Map<String, Class> getEntityFilesRecursive(File pFile, String packageName) {
        for (File file : pFile.listFiles()) {

            URL[] url = new URL[0];
            try {
                url = new URL[]{file.toURI().toURL()};
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (file.isDirectory()) {
                String path = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                getEntityFilesRecursive(file, path);
            } else {
                int substringLength = file.getName().indexOf('.');
                if (substringLength != -1) {
                    String className = file.getName().substring(0, substringLength);
                    URLClassLoader classLoader = new URLClassLoader(url);
                    Class currentClass = null;
                    try {
                        currentClass = classLoader.loadClass((!packageName.isEmpty() ?
                                (packageName + ".") : ("")) + className);

                        if (currentClass.isAnnotationPresent(SpringBootApplication.class)) {
                            setMainPath(currentClass.getPackage());
                        }

                        if (currentClass.isAnnotationPresent(Entity.class)) {
                            if (!entityClasses.containsKey(className)) {
                                entityClasses.put(className, currentClass);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        System.out.println(className + " not found!");
                    }
                }
            }
        }

        return entityClasses;
    }

    private static void createJavaFile(Map<String, Class> entityClassesMap, String repositorypostfix) {

        String postfix = repositorypostfix.trim().isEmpty() ? REPOSITORY_NAME : repositorypostfix.trim();

        for (Map.Entry<String, Class> classEntry : entityClassesMap.entrySet()) {
            Class currentClass = classEntry.getValue();
            String className = classEntry.getKey();
            Package packageName = currentClass.getPackage();
            String importEntityPackage = packageName.toString().substring(8);
            File directory = new File(SOURCE_PATH + "/" + MAIN_PATH + "/" + REPOSITORY_DIRECTORY_NAME);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File repoFile = new File(directory.getAbsolutePath(), className + postfix + ".java");
            if (!repoFile.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(repoFile, true))) {
                    StringBuilder builder = new StringBuilder(32);
                    builder.append(PACKAGE + MAIN_PATH.replaceAll("\\/","\\.") + "." + REPOSITORY_DIRECTORY_NAME + ";");
                    builder.append(System.lineSeparator());
                    builder.append(System.lineSeparator());
                    builder.append(REPOSITORY_IMPORT);
                    builder.append(System.lineSeparator());
                    builder.append(JPA_IMPORT);
                    builder.append(System.lineSeparator());
                    builder.append("import ").append(importEntityPackage).append(".").append(className).append(";");
                    builder.append(System.lineSeparator());
                    builder.append(System.lineSeparator());
                    builder.append(REPOSITORY_ANNOTATION);
                    builder.append(System.lineSeparator());
                    String idType = getIdType(currentClass);

                    if (idType.equals("")) {
                        writer.close();
                        Files.delete(repoFile.toPath());
                        continue;
                    } else {
                        builder.append(String.format(REPOSITORY_INTERFACE_NAME, className + postfix, className, idType));
                        builder.append(System.lineSeparator());
                        builder.append(CLOSE_BRACKET);
                        writer.write(builder.toString());
                        writer.flush();
                        writer.close();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

    private static String getIdType(Class currentClass) {
        int idCounter = 0;
        String idType = "";
        Method[] methods = currentClass.getDeclaredMethods();
        boolean hasMethodAnnotation = false;

        for (Method method : methods) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(Id.class)) {
                Class type = method.getReturnType();
                if (type.isPrimitive()) {
                    type = primitivesToWrapper.get(type);
                }
                idType = type.getTypeName();
                int indexLastDot = idType.lastIndexOf(".");
                if (indexLastDot != -1) {
                    idType = idType.substring(indexLastDot + 1);
                    hasMethodAnnotation = true;
                    idCounter++;
                }
            }
        }

        if (!hasMethodAnnotation) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Id.class)) {
                    Class type = field.getType();
                    if (type.isPrimitive()) {
                        type = primitivesToWrapper.get(type);
                    }
                    idType = type.getTypeName();
                    int indexLastDot = idType.lastIndexOf(".");
                    if (indexLastDot != -1) {
                        idType = idType.substring(indexLastDot + 1);
                        idCounter++;
                    }
                }
            }
        }

        if (idCounter == 1) {
            return idType;
        } else {
            return "";
        }
    }

    private static void setMainPath(Package packageSource) {
        String packageName = packageSource.toString().substring(8);
        MAIN_PATH = packageName.replaceAll("\\.", "/");
    }
}
