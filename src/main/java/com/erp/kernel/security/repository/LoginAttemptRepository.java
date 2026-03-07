package com.erp.kernel.security.repository;

import com.erp.kernel.security.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

/**
 * Repository for {@link LoginAttempt} entity persistence operations.
 */
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    /**
     * Finds login attempts for a username after a given time.
     *
     * @param username    the username
     * @param after       the earliest attempt time to include
     * @return the list of login attempts
     */
    List<LoginAttempt> findByUsernameAndAttemptedAtAfter(String username, Instant after);

    /**
     * Counts failed login attempts for a username after a given time.
     *
     * @param username the username
     * @param success  the success flag (should be false for failed attempts)
     * @param after    the earliest attempt time to include
     * @return the count of matching attempts
     */
    long countByUsernameAndSuccessAndAttemptedAtAfter(String username, boolean success, Instant after);
}
