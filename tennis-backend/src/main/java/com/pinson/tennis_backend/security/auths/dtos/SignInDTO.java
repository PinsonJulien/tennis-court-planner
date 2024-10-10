package com.pinson.tennis_backend.security.auths.dtos;

import com.pinson.tennis_backend.security.auths.dtos.requests.LoginRequest;

public record SignInDTO(
    String username,
    String password
) {
    public static SignInDTO from(LoginRequest loginRequest) {
        return new SignInDTO(
            loginRequest.username(),
            loginRequest.password()
        );
    }
}
