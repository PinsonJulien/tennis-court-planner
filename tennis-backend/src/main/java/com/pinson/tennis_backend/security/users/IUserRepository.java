package com.pinson.tennis_backend.security.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(
        @Param("username") String username
    );

    Optional<User> findByEmail(
        @Param("email") String email
    );

    boolean existsByUsername(
        @Param("username") String username
    );

    boolean existsByEmail(
        @Param("email") String email
    );
}
