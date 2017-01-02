package repository.builder.lib.annotations;

import repository.builder.lib.enums.OrderBy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderingBy {

    String name() default "";
    OrderBy type() default OrderBy.ASC;
}
