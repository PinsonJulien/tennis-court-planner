package com.pinson.tennis_backend.users.dtos;

public record CreateUserDTO(
    String username,
    String email,
    String password
) {
    //
}
