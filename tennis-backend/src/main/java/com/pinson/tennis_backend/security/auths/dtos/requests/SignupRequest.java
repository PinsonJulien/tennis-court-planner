package com.pinson.tennis_backend.security.auths.dtos.requests;

public record SignupRequest(
    String username,
    String email,
    String password
) {
    //
}
