package com.pinson.tennis_backend.users.dtos.requests;

import com.pinson.tennis_backend.users.dtos.CreateUserDTO;

public record CreateUserRequestDTO(
    String username,
    String email,
    String password
) {
    public static CreateUserDTO to (CreateUserRequestDTO createUserRequestDTO) {
        return new CreateUserDTO(
            createUserRequestDTO.username(),
            createUserRequestDTO.email(),
            createUserRequestDTO.password()
        );
    }
}
