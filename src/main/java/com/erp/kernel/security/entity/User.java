package com.erp.kernel.security.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Represents a system user for authentication and authorisation.
 *
 * <p>Users may authenticate via local credentials, LDAP, SSO, or passkeys.
 * Each user can be assigned multiple roles for role-based access control.
 */
@Entity
@Table(name = "auth_user")
public class User extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "display_name", length = 200)
    private String displayName;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "locked", nullable = false)
    private boolean locked;

    @Column(name = "ldap_dn", length = 500)
    private String ldapDn;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "password_changed_at")
    private Instant passwordChangedAt;

    @Column(name = "failed_login_count", nullable = false)
    private int failedLoginCount;

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
     * Returns the password hash.
     *
     * @return the password hash, or {@code null} for external authentication
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the password hash.
     *
     * @param passwordHash the password hash
     */
    public void setPasswordHash(final String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns the email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email the email address
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Returns the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName the last name
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name.
     *
     * @param displayName the display name
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns whether the user account is active.
     *
     * @return {@code true} if active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the user account is active.
     *
     * @param active the active flag
     */
    public void setActive(final boolean active) {
        this.active = active;
    }

    /**
     * Returns whether the user account is locked.
     *
     * @return {@code true} if locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Sets whether the user account is locked.
     *
     * @param locked the locked flag
     */
    public void setLocked(final boolean locked) {
        this.locked = locked;
    }

    /**
     * Returns the LDAP distinguished name.
     *
     * @return the LDAP DN, or {@code null} if not an LDAP user
     */
    public String getLdapDn() {
        return ldapDn;
    }

    /**
     * Sets the LDAP distinguished name.
     *
     * @param ldapDn the LDAP DN
     */
    public void setLdapDn(final String ldapDn) {
        this.ldapDn = ldapDn;
    }

    /**
     * Returns the last login timestamp.
     *
     * @return the last login instant, or {@code null} if never logged in
     */
    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    /**
     * Sets the last login timestamp.
     *
     * @param lastLoginAt the last login instant
     */
    public void setLastLoginAt(final Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    /**
     * Returns the password last changed timestamp.
     *
     * @return the password changed instant
     */
    public Instant getPasswordChangedAt() {
        return passwordChangedAt;
    }

    /**
     * Sets the password last changed timestamp.
     *
     * @param passwordChangedAt the password changed instant
     */
    public void setPasswordChangedAt(final Instant passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
    }

    /**
     * Returns the failed login attempt count.
     *
     * @return the failed login count
     */
    public int getFailedLoginCount() {
        return failedLoginCount;
    }

    /**
     * Sets the failed login attempt count.
     *
     * @param failedLoginCount the failed login count
     */
    public void setFailedLoginCount(final int failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }
}
