package com.erp.kernel.security.repository;

import com.erp.kernel.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for {@link User} entity persistence operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by username.
     *
     * @param username the username
     * @return the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a user exists with the given username.
     *
     * @param username the username
     * @return {@code true} if a user with the username exists
     */
    boolean existsByUsername(String username);

    /**
     * Checks whether a user exists with the given email.
     *
     * @param email the email address
     * @return {@code true} if a user with the email exists
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by email.
     *
     * @param email the email address
     * @return the user if found
     */
    Optional<User> findByEmail(String email);
}
