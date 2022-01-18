package com.github.mars05.crud.intellij.plugin.valid;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 枚举验证
 *
 * @author yu.xiao
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValidator.class})
public @interface Enum {

    Class<? extends java.lang.Enum<?>>[] value() default {};

    String enumField() default "code";

    String message() default "不被支持的值";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
