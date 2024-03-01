package com.codewithflow.exptracker.util.exception.validation;

import com.codewithflow.exptracker.dto.UserReqDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {}

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final UserReqDTO user = (UserReqDTO) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
