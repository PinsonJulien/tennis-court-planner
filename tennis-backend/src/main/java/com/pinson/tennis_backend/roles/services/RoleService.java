package com.pinson.tennis_backend.roles.services;

import com.pinson.tennis_backend.roles.dtos.CreateRoleDTO;
import com.pinson.tennis_backend.roles.dtos.RoleDTO;
import com.pinson.tennis_backend.roles.entities.Role;
import com.pinson.tennis_backend.roles.repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public boolean existsByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    @Override
    public List<RoleDTO> findAll() {
        final List<Role> roles = this.roleRepository.findAll();
        return RoleDTO.from(roles);
    }

    @Override
    public RoleDTO findById(Long id) {
        final Role role = this.roleRepository.findById(id).orElseThrow();
        return RoleDTO.from(role);
    }

    @Override
    public RoleDTO create(CreateRoleDTO roleDTO) {
        final Role role = Role.builder()
            .name(roleDTO.name())
            .build();

        final Role createdRole = this.roleRepository.save(role);
        return RoleDTO.from(createdRole);
    }

    @Override
    public RoleDTO findByName(String name) {
        final Role role = this.roleRepository.findByName(name).orElseThrow();
        return RoleDTO.from(role);
    }
}
