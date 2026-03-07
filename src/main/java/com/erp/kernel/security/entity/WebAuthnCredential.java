package com.erp.kernel.security.entity;

import com.erp.kernel.ddic.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents a WebAuthn/FIDO2 credential for passwordless authentication.
 *
 * <p>Stores the public key and metadata for passkeys and hardware security
 * keys (e.g., YubiKey) registered by a user.
 */
@Entity
@Table(name = "auth_webauthn_credential")
public class WebAuthnCredential extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "credential_id", nullable = false, unique = true, length = 500)
    private String credentialId;

    @Column(name = "public_key", nullable = false, length = 2000)
    private String publicKey;

    @Column(name = "credential_name", length = 200)
    private String credentialName;

    @Column(name = "sign_count", nullable = false)
    private long signCount;

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
     * Returns the credential ID.
     *
     * @return the Base64-encoded credential ID
     */
    public String getCredentialId() {
        return credentialId;
    }

    /**
     * Sets the credential ID.
     *
     * @param credentialId the Base64-encoded credential ID
     */
    public void setCredentialId(final String credentialId) {
        this.credentialId = credentialId;
    }

    /**
     * Returns the public key.
     *
     * @return the Base64-encoded public key
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Sets the public key.
     *
     * @param publicKey the Base64-encoded public key
     */
    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Returns the credential name.
     *
     * @return the human-readable credential name
     */
    public String getCredentialName() {
        return credentialName;
    }

    /**
     * Sets the credential name.
     *
     * @param credentialName the credential name
     */
    public void setCredentialName(final String credentialName) {
        this.credentialName = credentialName;
    }

    /**
     * Returns the signature counter.
     *
     * @return the sign count for replay attack prevention
     */
    public long getSignCount() {
        return signCount;
    }

    /**
     * Sets the signature counter.
     *
     * @param signCount the sign count
     */
    public void setSignCount(final long signCount) {
        this.signCount = signCount;
    }
}
