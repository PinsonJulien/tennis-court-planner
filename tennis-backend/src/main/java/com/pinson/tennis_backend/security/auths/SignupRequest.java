package com.pinson.tennis_backend.security.auths;

public record SignupRequest(
    String username,
    String email,
    String password
) {
    //
}
