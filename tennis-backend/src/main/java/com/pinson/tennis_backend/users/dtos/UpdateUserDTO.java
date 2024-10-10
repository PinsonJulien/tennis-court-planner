package com.pinson.tennis_backend.users.dtos;

public record UpdateUserDTO(
    String username,
    String email,
    String password
) {
    //
}
