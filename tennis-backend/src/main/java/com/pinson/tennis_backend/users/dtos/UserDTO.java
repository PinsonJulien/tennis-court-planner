package com.pinson.tennis_backend.users.dtos;

import com.pinson.tennis_backend.roles.dtos.RoleDTO;
import com.pinson.tennis_backend.users.entities.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record UserDTO(
    UUID id,
    String username,
    String email,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Optional<List<RoleDTO>> roles
) {
    public static UserDTO from(User user) {
        return from(user, true);
    }

    public static UserDTO from(User user, boolean includeRelations) {
        final UUID id = user.getId();
        final String username = user.getUsername();
        final String email = user.getEmail();
        final LocalDateTime createdAt = user.getCreatedAt();
        final LocalDateTime updatedAt = user.getUpdatedAt();
        Optional<List<RoleDTO>> roles = Optional.empty();

        if (includeRelations) {
            roles = Optional.of(RoleDTO.from(user.getRoles()));
        }

        return new UserDTO(id, username, email, createdAt, updatedAt, roles);
    }

    public static List<UserDTO> from(Collection<User> users) {
        return users.stream()
            .map(UserDTO::from)
            .toList();
    }

    public static List<UserDTO> from(Collection<User> users, boolean includeRelations) {
        return users.stream()
            .map(user -> from(user, includeRelations))
            .toList();
    }

}
