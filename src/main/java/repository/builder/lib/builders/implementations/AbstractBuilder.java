package repository.builder.lib.builders.implementations;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.Entity;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import static repository.builder.lib.constants.Constants.MAIN_PATH;
import static repository.builder.lib.constants.Constants.SOURCE_PATH;

abstract class AbstractBuilder {

    final static Map<Class<?>, Class<?>> primitivesToWrapper = new HashMap<>();
    static Map<String, Class> entityClasses = new HashMap<>();
    static Map<String, String> createdRepositories = new HashMap<>();

    static {
        primitivesToWrapper.put(boolean.class, Boolean.class);
        primitivesToWrapper.put(byte.class, Byte.class);
        primitivesToWrapper.put(short.class, Short.class);
        primitivesToWrapper.put(char.class, Character.class);
        primitivesToWrapper.put(int.class, Integer.class);
        primitivesToWrapper.put(long.class, Long.class);
        primitivesToWrapper.put(float.class, Float.class);
        primitivesToWrapper.put(double.class, Double.class);
        entityClasses = getEntityFilesRecursive(new File(SOURCE_PATH), MAIN_PATH);
    }

    protected abstract void build();

    @SuppressWarnings("ConstantConditions")
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
                                (packageName + ".") : (MAIN_PATH)) + className);

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

    private static void setMainPath(Package packageSource) {
        String packageName = packageSource.toString().substring(8);
        MAIN_PATH = packageName.replaceAll("\\.", "/");
    }
}
