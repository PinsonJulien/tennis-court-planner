package com.pinson.tennis_backend.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartialUpdateUserDTO {
    private String username;
    private String email;
    private String password;

    public boolean isUsernameSet() {
        return this.username != null;
    }

    public boolean isEmailSet() {
        return this.email != null;
    }

    public boolean isPasswordSet() {
        return this.password != null;
    }
}
