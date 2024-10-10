package com.pinson.tennis_backend.users.services;

import com.pinson.tennis_backend.roles.entities.Role;
import com.pinson.tennis_backend.roles.repositories.IRoleRepository;
import com.pinson.tennis_backend.users.dtos.CreateUserDTO;
import com.pinson.tennis_backend.users.dtos.PartialUpdateUserDTO;
import com.pinson.tennis_backend.users.dtos.UpdateUserDTO;
import com.pinson.tennis_backend.users.dtos.UserDTO;
import com.pinson.tennis_backend.users.entities.User;
import com.pinson.tennis_backend.users.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> findAll() {
        final List<User> users = this.userRepository.findAll();
        return this.createDTO(users);
    }

    @Override
    public UserDTO findById(UUID id) {
        final User user = this.getUser(id);
        return this.createDTO(user);
    }

    @Override
    public UserDTO findByEmail(String email) {
        final User user = this.userRepository.findByEmail(email).orElseThrow();
        return this.createDTO(user);
    }

    @Override
    public UserDTO findByUsername(String username) {
        final User user = this.userRepository.findByUsername(username).orElseThrow();
        return this.createDTO(user);
    }

    @Override
    public UserDTO create(CreateUserDTO user) {
        // Check for uniqueness of username and email
        if (this.userRepository.existsByUsername(user.username()))
            throw new IllegalArgumentException("Username already exists");
        if (this.userRepository.existsByEmail(user.email()))
            throw new IllegalArgumentException("Email already exists");

        final User newUser = User.builder()
            .username(user.username())
            .email(user.email())
            .password(this.encodePassword(user.password()))
            .build();

        final User createdUser = this.userRepository.save(newUser);
        return UserDTO.from(createdUser);
    }

    @Override
    public UserDTO update(UUID id, UpdateUserDTO user) {
        final User existingUser = this.userRepository.findById(id).orElseThrow();
        existingUser.setUsername(user.username());
        existingUser.setEmail(user.email());
        existingUser.setPassword(this.encodePassword(user.password()));

        final User updatedUser = this.userRepository.save(existingUser);
        return UserDTO.from(updatedUser);
    }

    @Override
    public UserDTO partialUpdate(UUID id, PartialUpdateUserDTO user) {
        final User existingUser = this.userRepository.findById(id).orElseThrow();
        if (user.isUsernameSet())
            existingUser.setUsername(user.getUsername());

        if (user.isEmailSet())
            existingUser.setEmail(user.getEmail());

        if (user.isPasswordSet())
            existingUser.setPassword(this.encodePassword(user.getPassword()));

        final User updatedUser = this.userRepository.save(existingUser);
        return UserDTO.from(updatedUser);
    }

    @Override
    public List<UserDTO> findAllDeleted() {
        final List<User> users = this.userRepository.findAllByDeletedAtIsNull();
        return this.createDTO(users);
    }

    @Override
    public void delete(UUID id) {
        final User user = this.userRepository.findById(id).orElseThrow();
        this.userRepository.delete(user);
    }

    @Override
    public UserDTO restore(UUID id) {
        final User user = this.userRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow();
        final User restoredUser = this.userRepository.restore(user);
        return this.createDTO(restoredUser);
    }

    @Override
    public boolean isUsernameValid(String username) {
        return !this.userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailValid(String email) {
        return !this.userRepository.existsByEmail(email);
    }

    @Override
    public UserDTO addRole(UUID userId, Long roleId) {
        final User user = this.getUser(userId);
        final Role role = this.getRole(roleId);

        if (user.hasRole(role))
            return this.createDTO(user);

        user.addRole(role);
        final User savedUser = this.userRepository.save(user);
        return this.createDTO(savedUser);
    }

    @Override
    public UserDTO removeRole(UUID userId, Long roleId) {
        final User user = this.getUser(userId);
        final Role role = this.getRole(roleId);

        if (!user.hasRole(role))
            return this.createDTO(user);

        user.removeRole(role);
        return this.createDTO(this.userRepository.save(user));
    }

    @Override
    public List<UserDTO> findUsersByRole(Long roleId) {
        final Role role = this.getRole(roleId);
        List<User> users = this.userRepository.findAllByUserRolesRole(role);
        return this.createDTO(users);
    }

    /**************************************************************************
    * Helper methods
    **************************************************************************/

    protected String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    protected User getUser(UUID id) {
        return this.userRepository.findById(id).orElseThrow();
    }

    protected Role getRole(Long id) {
        return this.roleRepository.findById(id).orElseThrow();
    }

    protected UserDTO createDTO(User user) {
        return UserDTO.from(user);
    }

    protected List<UserDTO> createDTO(Collection<User> users) {
        return UserDTO.from(users);
    }
}
