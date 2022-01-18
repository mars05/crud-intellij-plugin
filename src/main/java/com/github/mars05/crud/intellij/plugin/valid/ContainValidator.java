package com.github.mars05.crud.intellij.plugin.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yu.xiao
 */
public class ContainValidator implements ConstraintValidator<Contain, Object> {
    private List<Object> values;

    @Override
    public void initialize(Contain constraintAnnotation) {
        values = new ArrayList<>();
        for (Object i : constraintAnnotation.ints()) {
            values.add(i);
        }
        values.addAll(Arrays.asList(constraintAnnotation.strings()));
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value == null || values.contains(value);
    }
}
