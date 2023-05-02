package com.instacopy.instacopy.payload.request;

import com.instacopy.instacopy.annotations.PasswordMatchers;
import com.instacopy.instacopy.annotations.ValidEmail;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@PasswordMatchers
public class SignupRequest {

    @Email(message = "It shoud have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter your name")
    private String firstname;

    @NotEmpty(message = "Please enter your lastname")
    private String lastname;

    @NotEmpty(message = "Please enter your username")
    private String username;

    @NotEmpty(message = "User email is required")
    @Size(min = 6)
    private String password;
    private String confirmPassword;

}
