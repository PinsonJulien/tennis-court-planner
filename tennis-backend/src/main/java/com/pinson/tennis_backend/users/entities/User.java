package com.pinson.tennis_backend.users.entities;

import com.pinson.tennis_backend.roles.entities.Role;
import com.pinson.tennis_backend.users_roles.entities.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    /**************************************************************************
     * Fields
     **************************************************************************/

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="username", nullable = false, unique = true)
    private String username;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="previous_password", nullable = true)
    private String previousPassword;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    @Builder.Default
    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    private Set<UserRole> userRoles = new HashSet<>();

    /**************************************************************************
     * UserDetails methods
     **************************************************************************/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Add roles to the authorities
        return this.userRoles.stream()
            .map(UserRole::getRole)
            .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (this.deletedAt == null);
    }

    /**************************************************************************
     * Helper methods
     **************************************************************************/

    public void setPassword(String password) {
        // When password is set, save the previous password.
        if (this.isPasswordSet())
            this.setPreviousPassword(this.password);

        this.password = password;
    }

    public boolean isPasswordSet() {
        return this.password != null;
    }

    public void addRole(final Role role) {
        final UserRole userRole = UserRole.builder()
            .user(this)
            .role(role)
            .build();
        this.userRoles.add(userRole);
    }

    public void removeRole(final Role role) {
        this.userRoles.removeIf(userRole -> userRole.getRole().equals(role));
    }

    public boolean hasRole(final Role role) {
        return this.userRoles.stream().anyMatch(userRole -> userRole.getRole().equals(role));
    }

    public Set<Role> getRoles() {
        return this.userRoles.stream()
            .map(UserRole::getRole)
            .collect(Collectors.toSet());
    }
}
