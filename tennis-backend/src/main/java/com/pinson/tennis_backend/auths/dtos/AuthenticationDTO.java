package com.pinson.tennis_backend.auths.dtos;

import com.pinson.tennis_backend.users.dtos.UserDTO;

public record AuthenticationDTO (
    UserDTO user,
    JwtDTO jwt
) {
}
