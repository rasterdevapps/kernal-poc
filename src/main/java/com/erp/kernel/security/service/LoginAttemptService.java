package com.erp.kernel.security.service;

import com.erp.kernel.security.entity.LoginAttempt;
import com.erp.kernel.security.repository.LoginAttemptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Service for recording and querying login attempts.
 *
 * <p>Tracks both successful and failed login attempts for security
 * monitoring, account lockout, and audit compliance.
 */
@Service
@Transactional(readOnly = true)
public class LoginAttemptService {

    private static final Logger LOG = LoggerFactory.getLogger(LoginAttemptService.class);

    private final LoginAttemptRepository loginAttemptRepository;

    /**
     * Creates a new login attempt service.
     *
     * @param loginAttemptRepository the login attempt repository
     */
    public LoginAttemptService(final LoginAttemptRepository loginAttemptRepository) {
        this.loginAttemptRepository = Objects.requireNonNull(loginAttemptRepository,
                "loginAttemptRepository must not be null");
    }

    /**
     * Records a login attempt.
     *
     * @param username      the username
     * @param success       whether the login was successful
     * @param ipAddress     the IP address
     * @param userAgent     the user agent
     * @param failureReason the failure reason (null if successful)
     */
    @Transactional
    public void recordAttempt(final String username, final boolean success,
                              final String ipAddress, final String userAgent,
                              final String failureReason) {
        Objects.requireNonNull(username, "username must not be null");
        final var attempt = new LoginAttempt();
        attempt.setUsername(username);
        attempt.setSuccess(success);
        attempt.setIpAddress(ipAddress);
        attempt.setUserAgent(userAgent);
        attempt.setFailureReason(failureReason);
        attempt.setAttemptedAt(Instant.now());
        loginAttemptRepository.save(attempt);
        LOG.debug("Login attempt recorded for user '{}': success={}", username, success);
    }

    /**
     * Counts recent failed login attempts for a user.
     *
     * @param username the username
     * @param window   the time window to check
     * @return the count of failed attempts
     */
    public long countRecentFailedAttempts(final String username, final Duration window) {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(window, "window must not be null");
        final var since = Instant.now().minus(window);
        return loginAttemptRepository.countByUsernameAndSuccessAndAttemptedAtAfter(
                username, false, since);
    }
}
