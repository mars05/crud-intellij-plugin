package com.github.mars05.crud.intellij.plugin.valid;

import com.github.mars05.crud.intellij.plugin.util.Permit;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yu.xiao
 */
public class EnumValidator implements ConstraintValidator<Enum, Object> {
    private List<Object> values;

    @Override
    public void initialize(Enum constraintAnnotation) {
        values = new ArrayList<>();
        Class<? extends java.lang.Enum<?>>[] value = constraintAnnotation.value();
        for (Class<? extends java.lang.Enum<?>> clazz : value) {
            try {
                Method method = clazz.getMethod("values");
                java.lang.Enum<?>[] enums = (java.lang.Enum<?>[]) method.invoke(null);
                boolean exist = false;
                for (Field field : clazz.getDeclaredFields()) {
                    if (StringUtils.equals(field.getName(), constraintAnnotation.enumField())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    throw new RuntimeException("[" + constraintAnnotation.enumField() + "]枚举字段不存在");
                }

                for (java.lang.Enum<?> anEnum : enums) {
                    values.add(Permit.getField(clazz, constraintAnnotation.enumField()).get(anEnum));
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value == null || values.contains(value);
    }

}
