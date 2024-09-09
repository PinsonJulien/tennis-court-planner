package com.pinson.tennis_backend.security.users;

public record CreateUserDTO(
    String username,
    String email,
    String password
) {
    //
}
