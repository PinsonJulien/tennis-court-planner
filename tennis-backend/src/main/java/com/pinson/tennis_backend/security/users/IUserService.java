package com.pinson.tennis_backend.security.users;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {
    List<UserDTO> findAll();
    UserDTO findById(Long id);
    UserDTO findByEmail(String email);
    UserDTO findByUsername(String username);
    UserDTO create(CreateUserDTO userDTO);
}
