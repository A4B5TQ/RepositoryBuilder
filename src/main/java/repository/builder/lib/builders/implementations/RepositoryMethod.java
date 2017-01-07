package repository.builder.lib.builders.implementations;

import org.springframework.util.StringUtils;
import repository.builder.lib.annotations.order.Order;
import repository.builder.lib.annotations.order.OrderingBy;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static repository.builder.lib.builders.implementations.AbstractBuilder.primitivesToWrapper;
import static repository.builder.lib.constants.Constants.METHOD_SUFFIX_TYPE_PARAM;
import static repository.builder.lib.constants.RepositoryConstants.COLLECTION_RETURN_TYPE;
import static repository.builder.lib.constants.SpringMethodConstants.FIND_BY;
import static repository.builder.lib.constants.SpringMethodConstants.ORDER_BY;

public class RepositoryMethod {

    private Map<String, Map<String, Field>> fieldNameAndType;

    RepositoryMethod(Class clazz) {
        this.fieldNameAndType = new HashMap<>();
        this.scanForAnnotations(clazz);
    }

    private void scanForAnnotations(Class clazz) {
        this.getFieldNameAndType(clazz); //TODO merge with annotations get method
        //TODO get fields with Order and FindBy annotation
    }

    //Test scenario
    private String createOrderByMethods(String clazzType) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Map<String, Field>> fieldAndName : this.getFieldNameAndType().entrySet()) {
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

    //Test
//    private String createFindByMethods(String clazzType) {
//        StringBuilder builder = new StringBuilder(this.fieldNameAndType.size() * 3);
//        for (Map.Entry<String, Map<String, Field>> fieldAndName : this.fieldNameAndType.entrySet()) {
//            for (Map.Entry<String, Field> typeAndField : fieldAndName.getValue().entrySet()) {
//                builder.append(String.format(COLLECTION_RETURN_TYPE, clazzType));
//                builder.append(" ")
//                        .append(FIND_BY)
//                        .append(StringUtils.capitalize(fieldAndName.getKey()))
//                        .append(String.format(METHOD_SUFFIX_TYPE_PARAM,
//                                typeAndField.getKey(), fieldAndName.getKey()));
//                builder.append(System.lineSeparator());
//                builder.append(System.lineSeparator());
//            }
//        }
//        return builder.toString();
//    }

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

    private Map<String, Map<String, Field>> getFieldNameAndType() {
        return this.fieldNameAndType;
    }

    public void setFieldNameAndType(Map<String, Map<String, Field>> fieldNameAndType) {
        this.fieldNameAndType = fieldNameAndType;
    }
}
