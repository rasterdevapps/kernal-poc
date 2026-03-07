package com.erp.kernel.security.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents multi-factor authentication configuration for a user.
 *
 * <p>Stores the MFA type (e.g., TOTP) and the secret key used
 * for generating and validating one-time codes.
 */
@Entity
@Table(name = "auth_mfa_config")
public class MfaConfiguration extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "mfa_type", nullable = false, length = 20)
    private String mfaType;

    @Column(name = "secret_key", length = 500)
    private String secretKey;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "verified", nullable = false)
    private boolean verified;

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId the user ID
     */
    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the MFA type.
     *
     * @return the MFA type (e.g., TOTP)
     */
    public String getMfaType() {
        return mfaType;
    }

    /**
     * Sets the MFA type.
     *
     * @param mfaType the MFA type
     */
    public void setMfaType(final String mfaType) {
        this.mfaType = mfaType;
    }

    /**
     * Returns the secret key.
     *
     * @return the Base32-encoded secret key
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Sets the secret key.
     *
     * @param secretKey the Base32-encoded secret key
     */
    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Returns whether MFA is enabled.
     *
     * @return {@code true} if enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether MFA is enabled.
     *
     * @param enabled the enabled flag
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns whether MFA has been verified.
     *
     * @return {@code true} if verified
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * Sets whether MFA has been verified.
     *
     * @param verified the verified flag
     */
    public void setVerified(final boolean verified) {
        this.verified = verified;
    }
}
