package com.codewithflow.exptracker.util.exception.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class AtLeastOneFieldPresentValidator implements ConstraintValidator<AtLeastOneFieldPresent, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return Arrays.stream(value.getClass().getDeclaredFields())
                .anyMatch(field -> {
                    try {
                        field.setAccessible(true);
                        return field.get(value) != null;
                    } catch (Exception e) {
                        return false;
                    }
                });
    }
}
