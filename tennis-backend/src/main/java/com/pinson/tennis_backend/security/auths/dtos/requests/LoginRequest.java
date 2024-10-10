package com.pinson.tennis_backend.security.auths.dtos.requests;

public record LoginRequest(
    String username,
    String password
) {
    //
}
