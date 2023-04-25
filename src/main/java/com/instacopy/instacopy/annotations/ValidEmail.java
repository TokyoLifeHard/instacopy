package com.instacopy.instacopy.annotations;

import com.instacopy.instacopy.validations.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {
    String message() default "Invalid Email";
    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default{};
}
