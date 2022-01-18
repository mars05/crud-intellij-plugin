package com.github.mars05.crud.intellij.plugin.util;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * ValidateUtils
 *
 * @author yu.xiao
 */
public class ValidateUtils {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 校验对象注解
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws ValidationException 校验不通过异常
     */
    public static void validAnnotation(Object object, Class<?>... groups) throws ValidationException {
        validAnnotation(object, true, groups);
    }

    public static void validAnnotation(Object object, boolean isReportField, Class<?>... groups) throws ValidationException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint = constraintViolations.iterator()
                    .next();
            String message = constraint.getMessage();
            String propertyPath = constraint.getPropertyPath().toString();
            if (!StringUtils.contains(message, propertyPath) && isReportField) {
                message = "[" + propertyPath + "]" + message;
            }
            throw new ValidationException(message);
        }
    }

}