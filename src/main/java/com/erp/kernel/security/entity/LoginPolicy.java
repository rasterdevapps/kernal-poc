package com.erp.kernel.security.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a login policy defining password complexity, lockout, and session rules.
 *
 * <p>Administrators can define multiple policies and assign them at user,
 * group, or organisational unit levels to enforce security requirements.
 */
@Entity
@Table(name = "auth_login_policy")
public class LoginPolicy extends BaseEntity {

    @Column(name = "policy_name", nullable = false, unique = true, length = 100)
    private String policyName;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "min_password_length", nullable = false)
    private int minPasswordLength = 8;

    @Column(name = "require_uppercase", nullable = false)
    private boolean requireUppercase = true;

    @Column(name = "require_lowercase", nullable = false)
    private boolean requireLowercase = true;

    @Column(name = "require_digit", nullable = false)
    private boolean requireDigit = true;

    @Column(name = "require_special_char", nullable = false)
    private boolean requireSpecialChar = true;

    @Column(name = "password_expiry_days", nullable = false)
    private int passwordExpiryDays = 90;

    @Column(name = "max_failed_attempts", nullable = false)
    private int maxFailedAttempts = 5;

    @Column(name = "lockout_duration_minutes", nullable = false)
    private int lockoutDurationMinutes = 30;

    @Column(name = "session_timeout_minutes", nullable = false)
    private int sessionTimeoutMinutes = 480;

    @Column(name = "enforce_mfa", nullable = false)
    private boolean enforceMfa;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    /**
     * Returns the policy name.
     *
     * @return the policy name
     */
    public String getPolicyName() {
        return policyName;
    }

    /**
     * Sets the policy name.
     *
     * @param policyName the policy name
     */
    public void setPolicyName(final String policyName) {
        this.policyName = policyName;
    }

    /**
     * Returns the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns the minimum password length.
     *
     * @return the minimum password length
     */
    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    /**
     * Sets the minimum password length.
     *
     * @param minPasswordLength the minimum password length
     */
    public void setMinPasswordLength(final int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    /**
     * Returns whether uppercase characters are required.
     *
     * @return {@code true} if uppercase is required
     */
    public boolean isRequireUppercase() {
        return requireUppercase;
    }

    /**
     * Sets whether uppercase characters are required.
     *
     * @param requireUppercase the require uppercase flag
     */
    public void setRequireUppercase(final boolean requireUppercase) {
        this.requireUppercase = requireUppercase;
    }

    /**
     * Returns whether lowercase characters are required.
     *
     * @return {@code true} if lowercase is required
     */
    public boolean isRequireLowercase() {
        return requireLowercase;
    }

    /**
     * Sets whether lowercase characters are required.
     *
     * @param requireLowercase the require lowercase flag
     */
    public void setRequireLowercase(final boolean requireLowercase) {
        this.requireLowercase = requireLowercase;
    }

    /**
     * Returns whether digits are required.
     *
     * @return {@code true} if digits are required
     */
    public boolean isRequireDigit() {
        return requireDigit;
    }

    /**
     * Sets whether digits are required.
     *
     * @param requireDigit the require digit flag
     */
    public void setRequireDigit(final boolean requireDigit) {
        this.requireDigit = requireDigit;
    }

    /**
     * Returns whether special characters are required.
     *
     * @return {@code true} if special characters are required
     */
    public boolean isRequireSpecialChar() {
        return requireSpecialChar;
    }

    /**
     * Sets whether special characters are required.
     *
     * @param requireSpecialChar the require special char flag
     */
    public void setRequireSpecialChar(final boolean requireSpecialChar) {
        this.requireSpecialChar = requireSpecialChar;
    }

    /**
     * Returns the password expiry period in days.
     *
     * @return the password expiry days
     */
    public int getPasswordExpiryDays() {
        return passwordExpiryDays;
    }

    /**
     * Sets the password expiry period in days.
     *
     * @param passwordExpiryDays the password expiry days
     */
    public void setPasswordExpiryDays(final int passwordExpiryDays) {
        this.passwordExpiryDays = passwordExpiryDays;
    }

    /**
     * Returns the maximum number of failed login attempts before lockout.
     *
     * @return the max failed attempts
     */
    public int getMaxFailedAttempts() {
        return maxFailedAttempts;
    }

    /**
     * Sets the maximum number of failed login attempts before lockout.
     *
     * @param maxFailedAttempts the max failed attempts
     */
    public void setMaxFailedAttempts(final int maxFailedAttempts) {
        this.maxFailedAttempts = maxFailedAttempts;
    }

    /**
     * Returns the lockout duration in minutes.
     *
     * @return the lockout duration in minutes
     */
    public int getLockoutDurationMinutes() {
        return lockoutDurationMinutes;
    }

    /**
     * Sets the lockout duration in minutes.
     *
     * @param lockoutDurationMinutes the lockout duration in minutes
     */
    public void setLockoutDurationMinutes(final int lockoutDurationMinutes) {
        this.lockoutDurationMinutes = lockoutDurationMinutes;
    }

    /**
     * Returns the session timeout in minutes.
     *
     * @return the session timeout in minutes
     */
    public int getSessionTimeoutMinutes() {
        return sessionTimeoutMinutes;
    }

    /**
     * Sets the session timeout in minutes.
     *
     * @param sessionTimeoutMinutes the session timeout in minutes
     */
    public void setSessionTimeoutMinutes(final int sessionTimeoutMinutes) {
        this.sessionTimeoutMinutes = sessionTimeoutMinutes;
    }

    /**
     * Returns whether MFA is enforced.
     *
     * @return {@code true} if MFA is enforced
     */
    public boolean isEnforceMfa() {
        return enforceMfa;
    }

    /**
     * Sets whether MFA is enforced.
     *
     * @param enforceMfa the enforce MFA flag
     */
    public void setEnforceMfa(final boolean enforceMfa) {
        this.enforceMfa = enforceMfa;
    }

    /**
     * Returns whether the policy is active.
     *
     * @return {@code true} if active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the policy is active.
     *
     * @param active the active flag
     */
    public void setActive(final boolean active) {
        this.active = active;
    }
}
