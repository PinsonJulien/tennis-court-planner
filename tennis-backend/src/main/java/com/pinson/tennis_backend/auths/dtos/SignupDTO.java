package com.pinson.tennis_backend.auths.dtos;

import com.pinson.tennis_backend.users.dtos.CreateUserDTO;

public record SignupDTO(
    String username,
    String email,
    String password
) {
    public CreateUserDTO toCreateUserDTO() {
        return new CreateUserDTO(username, email, password);
    }
}
