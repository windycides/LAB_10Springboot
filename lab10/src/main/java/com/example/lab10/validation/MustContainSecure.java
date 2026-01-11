package com.example.lab10.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD}) // We can use this on variables
@Retention(RetentionPolicy.RUNTIME) // It works while the app is running
@Constraint(validatedBy = SecureWordValidator.class) // Links to the logic class we will create next
public @interface MustContainSecure {
    // Default error message
    String message() default "Password must contain the word 'secure'";

    // Boilerplate code required by Spring Validation
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}