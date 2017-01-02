package repository.builder.lib.builders.implementations;

import org.springframework.util.StringUtils;
import repository.builder.lib.annotations.Order;
import repository.builder.lib.annotations.OrderingBy;

import javax.persistence.Id;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static repository.builder.lib.constants.Constants.*;
import static repository.builder.lib.constants.RepositoryConstants.*;
import static repository.builder.lib.constants.SpringMethodConstants.FIND_BY;
import static repository.builder.lib.constants.SpringMethodConstants.ORDER_BY;

public class RepositoryBuilder extends AbstractBuilder {

    private Map<String, Map<String, Field>> fieldNameAndType = new HashMap<>();
    private String repositoryPostfix;
    private Boolean withMethods;

    public RepositoryBuilder(String repositoryPostfix, Boolean withMethods) {
        this.setRepositoryPostfix(repositoryPostfix);
        this.setWithMethods(withMethods);
    }

    public void build() {
        this.createRepositoryJavaFile(entityClasses);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
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
                this.getFieldNameAndType(currentClass);
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
                        builder.append(System.lineSeparator());
                        if (this.getWithMethods()) {
                            builder.append(this.createFindByMethods(className));
                            builder.append(this.createOrderByMethods(className));
                        }
                        builder.append(System.lineSeparator());
                        builder.append(CLOSE_BRACKET);
                        writer.write(builder.toString());
                        writer.flush();
                        writer.close();
                        createdRepositories.put(className, repoFileName);
                        this.fieldNameAndType.clear();
                    } else {
                        writer.close();
                        Files.delete(repoFile.toPath());
                        this.fieldNameAndType.clear();
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

    private void setWithMethods(Boolean withMethods) {
        this.withMethods = withMethods;
    }

    private Boolean getWithMethods() {
        return this.withMethods;
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

    private void getFieldNameAndType(Class currentClass) {
        Field[] fields = currentClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            String name = field.getName();
            Class<?> type = field.getType();

            if (type.isPrimitive()) {
                type = primitivesToWrapper.get(type);
                Map<String, Field> typeAndField = new HashMap<>();
                typeAndField.put(type.getSimpleName(), field);
                this.fieldNameAndType.put(name, typeAndField);
            }
//            } else if (!Collection.class.isAssignableFrom(type)) {
//                fieldNameAndType.put(name, type.getSimpleName());
//            }
        }
    }

    private String createFindByMethods(String clazzType) {
        StringBuilder builder = new StringBuilder(this.fieldNameAndType.size() * 3);
        for (Map.Entry<String, Map<String, Field>> fieldAndName : this.fieldNameAndType.entrySet()) {
            for (Map.Entry<String, Field> typeAndField : fieldAndName.getValue().entrySet()) {
                builder.append(String.format(COLLECTION_RETURN_TYPE, clazzType));
                builder.append(" ")
                        .append(FIND_BY)
                        .append(StringUtils.capitalize(fieldAndName.getKey()))
                        .append(String.format(METHOD_SUFFIX_TYPE_PARAM,
                                typeAndField.getKey(), fieldAndName.getKey()));
                builder.append(System.lineSeparator());
                builder.append(System.lineSeparator());
            }
        }
        return builder.toString();
    }

    private String createOrderByMethods(String clazzType) {
        StringBuilder builder = new StringBuilder(this.fieldNameAndType.size() * 3);
        for (Map.Entry<String, Map<String, Field>> fieldAndName : this.fieldNameAndType.entrySet()) {
            for (Map.Entry<String, Field> typeAndField : fieldAndName.getValue().entrySet()) {
                Field field = typeAndField.getValue();
                if (field.isAnnotationPresent(Order.class)) {
                    builder.append(String.format(COLLECTION_RETURN_TYPE, clazzType));
                    Order order = field.getAnnotation(Order.class);
                    builder.append(" ")
                            .append(FIND_BY)
                            .append(StringUtils.capitalize(fieldAndName.getKey()))
                            .append(ORDER_BY);

                    for (OrderingBy orderBy : order.orderBy()) {
                        builder.append(StringUtils.capitalize(orderBy.name()));
                        switch (orderBy.type()) {
                            case ASC:
                                builder.append("Asc");
                                break;
                            case DESC:
                                builder.append("Desc");
                                break;
                        }
                    }
                    builder.append(String.format(METHOD_SUFFIX_TYPE_PARAM,
                            typeAndField.getKey(), fieldAndName.getKey()));
                    builder.append(System.lineSeparator());
                    builder.append(System.lineSeparator());
                }

            }
        }
        return builder.toString();
    }
}
