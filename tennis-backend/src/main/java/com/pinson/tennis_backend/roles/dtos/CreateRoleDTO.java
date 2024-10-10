package com.pinson.tennis_backend.roles.dtos;

import com.pinson.tennis_backend.roles.enums.RoleEnum;

import java.util.HashSet;
import java.util.Set;

public record CreateRoleDTO(
    String name
) {
    public static CreateRoleDTO from(RoleEnum name) {
        return new CreateRoleDTO(name.name());
    }

    public static Set<CreateRoleDTO> from(Set<RoleEnum> names) {
        final Set<CreateRoleDTO> roles = new HashSet<>();

        names.forEach(
            name -> roles.add(from(name))
        );

        return roles;
    }
}
