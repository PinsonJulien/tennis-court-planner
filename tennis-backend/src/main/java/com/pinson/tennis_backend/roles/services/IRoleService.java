package com.pinson.tennis_backend.roles.services;

import com.pinson.tennis_backend.roles.dtos.CreateRoleDTO;
import com.pinson.tennis_backend.roles.dtos.RoleDTO;

import java.util.List;

public interface IRoleService {
    List<RoleDTO> findAll();
    RoleDTO findById(Long id);
    RoleDTO create(CreateRoleDTO roleDTO);

    boolean existsByName(String name);
    RoleDTO findByName(String name);
}
