package com.pinson.tennis_backend.security.users;

import java.util.List;

public record UserDTO(
    String username,
    String email
) {
    public static UserDTO from(User user) {
        return new UserDTO(
            user.getUsername(),
            user.getEmail()
        );
    }

    public static List<UserDTO> from(List<User> users) {
        return users.stream()
            .map(UserDTO::from)
            .toList();
    }
}
