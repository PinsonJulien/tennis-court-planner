package com.pinson.tennis_backend.security.auths;

public record LoginRequest(
    String username,
    String password
) {
    //
}
