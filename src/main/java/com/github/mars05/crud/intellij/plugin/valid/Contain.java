package com.github.mars05.crud.intellij.plugin.valid;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 可以用来限制枚举值
 *
 * @author yu.xiao
 * @see javax.validation.constraints.Pattern
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ContainValidator.class})
public @interface Contain {
    int[] ints() default {};

    String[] strings() default {};

    String message() default "不被允许的值";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
