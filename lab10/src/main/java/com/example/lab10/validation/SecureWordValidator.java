package com.example.lab10.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SecureWordValidator implements ConstraintValidator<MustContainSecure, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // If input is null, we usually let @NotNull handle it, so return true here
        if (value == null) {
            return true;
        }
        // The Logic: Return true only if the string contains "secure" (case insensitive)
        return value.toLowerCase().contains("secure");
    }
}