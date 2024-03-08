package com.codewithflow.exptracker.util.exception.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = AtLeastOneFieldPresentValidator.class)
public @interface AtLeastOneFieldPresent {
    String message() default "At least one field must be present.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
