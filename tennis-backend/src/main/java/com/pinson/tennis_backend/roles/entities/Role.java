package com.pinson.tennis_backend.roles.entities;

import com.pinson.tennis_backend.users.entities.User;
import com.pinson.tennis_backend.users_roles.entities.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role implements GrantedAuthority {

    /**************************************************************************
     * Fields
     **************************************************************************/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Builder.Default
    @OneToMany(
        mappedBy = "role",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<UserRole> userRoles = new HashSet<>();

    /**************************************************************************
     * GrantedAuthority implementation
     **************************************************************************/

    @Override
    public String getAuthority() {
        final String prefix = "ROLE_";
        return prefix + this.name;
    }

    /**************************************************************************
     * Helper methods
     **************************************************************************/

    public Set<User> getUsers() {
        final Set<User> users = new HashSet<>();
        for (UserRole userRole : this.userRoles) {
            users.add(userRole.getUser());
        }
        return users;
    }

}
