package com.pinson.tennis_backend.auths.dtos.requests;

import com.pinson.tennis_backend.auths.dtos.LoginDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public LoginDTO toLoginDTO() {
        return new LoginDTO(this.username, this.password);
    }
}
