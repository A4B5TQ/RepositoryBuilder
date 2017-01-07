package repository.builder.lib.annotations.findBy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Clause {

    String fieldName() default "";

    boolean isNull() default false;

    boolean notNull() default true;
}
