package com.pinson.tennis_backend.users.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddRoleToUserRequestDTO {
    @NotNull(message = "User ID is required")
    private Long roleId;
}
