package com.pinson.tennis_backend.security.auths.dtos;

import com.pinson.tennis_backend.users.dtos.CreateUserDTO;

public record SignUpDTO(
    CreateUserDTO user
) {
    //
}
