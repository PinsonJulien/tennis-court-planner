package com.pinson.tennis_backend.security.jwts;

public record JwtResponse(
    String token,
    String username,
    String type
) {
    public JwtResponse(
        String token,
        String username
    ) {
        this(token, username, "Bearer");
    }
}
