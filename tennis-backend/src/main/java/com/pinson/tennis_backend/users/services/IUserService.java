package com.pinson.tennis_backend.users.services;

import com.pinson.tennis_backend.users.dtos.CreateUserDTO;
import com.pinson.tennis_backend.users.dtos.PartialUpdateUserDTO;
import com.pinson.tennis_backend.users.dtos.UpdateUserDTO;
import com.pinson.tennis_backend.users.dtos.UserDTO;
import com.pinson.tennis_backend.users.entities.User;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    List<UserDTO> findAll();
    UserDTO findById(UUID id);
    UserDTO findByEmail(String email);
    UserDTO findByUsername(String username);

    UserDTO create(CreateUserDTO user);
    UserDTO update(UUID id, UpdateUserDTO user);
    UserDTO partialUpdate(UUID id, PartialUpdateUserDTO user);

    List<UserDTO> findAllDeleted();
    void delete(UUID id);
    UserDTO restore(UUID id);

    boolean isUsernameValid(String username);
    boolean isEmailValid(String email);

    UserDTO addRole(UUID userId, Long roleId);
    UserDTO removeRole(UUID userId, Long roleId);
    List<UserDTO> findUsersByRole(Long roleId);
}
