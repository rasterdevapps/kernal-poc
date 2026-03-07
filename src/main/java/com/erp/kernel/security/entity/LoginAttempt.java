package com.erp.kernel.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a login attempt for audit and security tracking.
 *
 * <p>Tracks both successful and failed login attempts including IP address,
 * user agent, and failure reason for security monitoring and compliance.
 */
@Entity
@Table(name = "auth_login_attempt")
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "success", nullable = false)
    private boolean success;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "failure_reason", length = 200)
    private String failureReason;

    @Column(name = "attempted_at", nullable = false)
    private Instant attemptedAt;

    /**
     * Returns the entity identifier.
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the entity identifier.
     *
     * @param id the ID
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Returns whether the login was successful.
     *
     * @return {@code true} if successful
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets whether the login was successful.
     *
     * @param success the success flag
     */
    public void setSuccess(final boolean success) {
        this.success = success;
    }

    /**
     * Returns the IP address of the login attempt.
     *
     * @return the IP address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the IP address.
     *
     * @param ipAddress the IP address
     */
    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Returns the user agent string.
     *
     * @return the user agent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Sets the user agent string.
     *
     * @param userAgent the user agent
     */
    public void setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * Returns the failure reason.
     *
     * @return the failure reason, or {@code null} if successful
     */
    public String getFailureReason() {
        return failureReason;
    }

    /**
     * Sets the failure reason.
     *
     * @param failureReason the failure reason
     */
    public void setFailureReason(final String failureReason) {
        this.failureReason = failureReason;
    }

    /**
     * Returns the attempt timestamp.
     *
     * @return the attempted instant
     */
    public Instant getAttemptedAt() {
        return attemptedAt;
    }

    /**
     * Sets the attempt timestamp.
     *
     * @param attemptedAt the attempted instant
     */
    public void setAttemptedAt(final Instant attemptedAt) {
        this.attemptedAt = attemptedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var that = (LoginAttempt) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
