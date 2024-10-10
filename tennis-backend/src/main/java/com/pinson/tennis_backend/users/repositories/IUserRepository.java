package com.pinson.tennis_backend.users.repositories;

import com.pinson.tennis_backend.roles.entities.Role;
import com.pinson.tennis_backend.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(
        @Param("username")
        String username
    );

    boolean existsByEmail(
        @Param("email")
        String email
    );

    Optional<User> findByUsername(
        @Param("username")
        String username
    );

    Optional<User> findByEmail(
        @Param("email")
        String email
    );

    // findAll non deleted users
    List<User> findAllByDeletedAtIsNull();

    // find user by id and not deleted
    Optional<User> findByIdAndDeletedAtIsNull(
        @Param("id")
        UUID id
    );

    Optional<User> findByIdAndDeletedAtIsNotNull(
        @Param("id")
        UUID id
    );

    // find all by role
    List<User> findAllByUserRolesRole(
        @Param("role")
        Role role
    );

    // override delete method to set deletedAt
    @Override
    default void delete(User user) {
        user.setDeletedAt(java.time.LocalDateTime.now());
        save(user);
    }

    // override default getAll method to return only non deleted users
    @Override
    default List<User> findAll() {
        return findAllByDeletedAtIsNull();
    }

    // override default findById method to return only non deleted user
    @Override
    default Optional<User> findById(UUID id) {
        return findByIdAndDeletedAtIsNull(id);
    }

    default User restore(User user) {
        user.setDeletedAt(null);
        return save(user);
    }

    default void hardDelete(User user) {
        delete(user);
    }

}
