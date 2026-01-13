package com.example.lab10.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SecureWordValidator.class)
public @interface MustContainSecure {

    String message() default "Password must contain the word 'secure'";


    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}