package com.pinson.tennis_backend.roles.dtos;

import com.pinson.tennis_backend.roles.entities.Role;

import java.util.Collection;
import java.util.List;

public record RoleDTO(
    Long id,
    String name
) {
    public static RoleDTO from(Role role) {
        return from(role, true);
    }

    public static RoleDTO from(Role role, boolean includeRelations) {
        final Long id = role.getId();
        final String name = role.getName();

        return new RoleDTO(id, name);
    }

    public static List<RoleDTO> from(Collection<Role> roles) {
        System.out.println("RoleDTO.from(Collection<Role> roles)");
        System.out.println(roles);
        return roles.stream()
            .map(RoleDTO::from)
            .toList();
    }

}
