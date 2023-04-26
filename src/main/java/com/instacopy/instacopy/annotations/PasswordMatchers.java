package com.instacopy.instacopy.annotations;

import com.instacopy.instacopy.validations.PasswordMatchersValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchersValidator.class)
public @interface PasswordMatchers {
    String message() default "Password dont match";
    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default{};
}
