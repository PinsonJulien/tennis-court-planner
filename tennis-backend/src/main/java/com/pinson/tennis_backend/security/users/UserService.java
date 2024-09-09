package com.pinson.tennis_backend.security.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found")
            );
    }

    @Override
    public List<UserDTO> findAll() {
        final List<User> users = this.userRepository.findAll();
        return UserDTO.from(users);
    }

    @Override
    public UserDTO findById(Long id) {
        final User user = this.userRepository.findById(id).orElseThrow();
        return UserDTO.from(user);
    }

    @Override
    public UserDTO findByEmail(String email) {
        final User user = this.userRepository.findByEmail(email).orElseThrow();
        return UserDTO.from(user);
    }

    @Override
    public UserDTO findByUsername(String username) {
        final User user = this.userRepository.findByUsername(username).orElseThrow();
        return UserDTO.from(user);
    }

    @Override
    public UserDTO create(CreateUserDTO userDTO) {
        // Check for uniqueness of username and email
        if (this.userRepository.existsByUsername(userDTO.username()))
            throw new IllegalArgumentException("Username already exists");
        if (this.userRepository.existsByEmail(userDTO.email()))
            throw new IllegalArgumentException("Email already exists");

        final User user = User.builder()
            .username(userDTO.username())
            .email(userDTO.email())
            .password(userDTO.password())
            .build();

        final User savedUser = this.userRepository.save(user);
        return UserDTO.from(savedUser);
    }
}
