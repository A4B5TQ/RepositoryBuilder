package repository.builder.lib.builders.implementations;

import javax.persistence.Id;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Map;

import static repository.builder.lib.constants.Constants.*;
import static repository.builder.lib.constants.RepositoryConstants.*;

public class RepositoryBuilder extends AbstractBuilder {

    private String repositoryPostfix;

    public RepositoryBuilder(String repositoryPostfix) {
        this.setRepositoryPostfix(repositoryPostfix);
    }

    public void build() {
        this.createRepositoryJavaFile(entityClasses);
    }

    private void createRepositoryJavaFile(Map<String, Class> entityClassesMap) {

        String postfix = this.getRepositoryPostfix().isEmpty() ? REPOSITORY_NAME : this.getRepositoryPostfix();

        for (Map.Entry<String, Class> classEntry : entityClassesMap.entrySet()) {
            Class currentClass = classEntry.getValue();
            String className = classEntry.getKey();
            Package packageName = currentClass.getPackage();
            String importEntityPackage = packageName.toString().substring(8);
            File directory = new File(SOURCE_PATH + "/" + MAIN_PATH + "/" + REPOSITORY_DIRECTORY_NAME);
            if (!directory.exists()) {
                directory.mkdir();
            }
            String repoFileName = className + postfix;
            File repoFile = new File(directory.getAbsolutePath(), className + postfix + ".java");
            if (!repoFile.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(repoFile, true))) {
                    String idType = this.getIdType(currentClass);
                    StringBuilder builder = new StringBuilder(32);
                    if (!idType.equals("")) {
                        builder.append(PACKAGE).append(MAIN_PATH.replaceAll("/", "\\.")).append(".").append(REPOSITORY_DIRECTORY_NAME).append(";");
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
                        builder.append(String.format(REPOSITORY_INTERFACE_NAME, className + postfix, className, idType));
                        builder.append(System.lineSeparator());
                        //TODO implements some methods
                        builder.append(CLOSE_BRACKET);
                        writer.write(builder.toString());
                        writer.flush();
                        writer.close();
                        createdRepositories.put(className,repoFileName);
                    } else {
                        writer.close();
                        Files.delete(repoFile.toPath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getIdType(Class currentClass) {
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

    private String getRepositoryPostfix() {
        return this.repositoryPostfix;
    }

    private void setRepositoryPostfix(String repositoryPostfix) {
        if (repositoryPostfix != null) {
            this.repositoryPostfix = repositoryPostfix.trim();
        } else {
            this.repositoryPostfix = "";
        }
    }
}
