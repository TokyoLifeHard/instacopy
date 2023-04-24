package com.instacopy.instacopy.payload.response;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {

    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = "Invalid login";
        this.password = "Invalid password";
    }
}
