package com.instacopy.instacopy.validations;

import com.instacopy.instacopy.annotations.PasswordMatchers;
import com.instacopy.instacopy.payload.request.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchersValidator implements ConstraintValidator<PasswordMatchers,Object> {
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest userSignupRequest = (SignupRequest) obj;
        System.out.println(userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword()));
        return userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword());
    }

    @Override
    public void initialize(PasswordMatchers constraintAnnotation) {

    }
}
