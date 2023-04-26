package com.instacopy.instacopy.validations;

import com.instacopy.instacopy.annotations.PasswordMatchers;
import com.instacopy.instacopy.payload.request.SighupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchersValidator implements ConstraintValidator<PasswordMatchers,Object> {
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        SighupRequest sighupRequest = (SighupRequest) o;
        return sighupRequest.getPassword().equals(sighupRequest.getConfimPassword());
    }

    @Override
    public void initialize(PasswordMatchers constraintAnnotation) {

    }
}
