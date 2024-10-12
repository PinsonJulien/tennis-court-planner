package com.pinson.tennis_backend.auths.dtos.requests;

import com.pinson.tennis_backend.auths.dtos.SignupDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    public SignupDTO toSignupDTO() {
        return new SignupDTO(this.username, this.email, this.password);
    }

}
