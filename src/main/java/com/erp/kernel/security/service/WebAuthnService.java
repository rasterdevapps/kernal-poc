package com.erp.kernel.security.service;

import com.erp.kernel.security.entity.WebAuthnCredential;
import com.erp.kernel.security.repository.WebAuthnCredentialRepository;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 * Service managing WebAuthn/FIDO2 credential registration and authentication.
 *
 * <p>Supports passkey-based passwordless login and hardware security key
 * authentication (e.g., YubiKey). Implements the registration and
 * authentication ceremonies defined by the WebAuthn specification.
 */
@Service
@Transactional(readOnly = true)
public class WebAuthnService {

    private static final Logger LOG = LoggerFactory.getLogger(WebAuthnService.class);
    private static final int CREDENTIAL_ID_LENGTH = 32;

    private final WebAuthnCredentialRepository webAuthnCredentialRepository;

    /**
     * Creates a new WebAuthn service.
     *
     * @param webAuthnCredentialRepository the WebAuthn credential repository
     */
    public WebAuthnService(final WebAuthnCredentialRepository webAuthnCredentialRepository) {
        this.webAuthnCredentialRepository = Objects.requireNonNull(webAuthnCredentialRepository,
                "webAuthnCredentialRepository must not be null");
    }

    /**
     * Registers a new WebAuthn credential for a user.
     *
     * @param userId         the user ID
     * @param publicKey      the Base64-encoded public key
     * @param credentialName the human-readable credential name
     * @return the registered credential
     */
    @Transactional
    public WebAuthnCredential registerCredential(final Long userId, final String publicKey,
                                                 final String credentialName) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(publicKey, "publicKey must not be null");

        final var credential = new WebAuthnCredential();
        credential.setUserId(userId);
        credential.setCredentialId(generateCredentialId());
        credential.setPublicKey(publicKey);
        credential.setCredentialName(credentialName);
        credential.setSignCount(0L);

        final var saved = webAuthnCredentialRepository.save(credential);
        LOG.info("WebAuthn credential registered for user ID {}: '{}'", userId, credentialName);
        return saved;
    }

    /**
     * Returns all WebAuthn credentials for a user.
     *
     * @param userId the user ID
     * @return the list of credentials
     */
    public List<WebAuthnCredential> getCredentials(final Long userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return webAuthnCredentialRepository.findByUserId(userId);
    }

    /**
     * Validates a WebAuthn authentication assertion.
     *
     * @param credentialId the credential ID used in the assertion
     * @param signCount    the new signature counter from the authenticator
     * @return {@code true} if the assertion is valid
     */
    @Transactional
    public boolean validateAssertion(final String credentialId, final long signCount) {
        Objects.requireNonNull(credentialId, "credentialId must not be null");

        final var credential = webAuthnCredentialRepository.findByCredentialId(credentialId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "WebAuthn credential not found: %s".formatted(credentialId)));

        if (signCount <= credential.getSignCount()) {
            LOG.warn("WebAuthn replay attack detected for credential '{}'", credentialId);
            return false;
        }

        credential.setSignCount(signCount);
        webAuthnCredentialRepository.save(credential);
        LOG.debug("WebAuthn assertion validated for credential '{}'", credentialId);
        return true;
    }

    /**
     * Deletes a WebAuthn credential.
     *
     * @param credentialId the credential ID to delete
     * @throws EntityNotFoundException if the credential is not found
     */
    @Transactional
    public void deleteCredential(final Long credentialId) {
        Objects.requireNonNull(credentialId, "credentialId must not be null");
        if (!webAuthnCredentialRepository.existsById(credentialId)) {
            throw new EntityNotFoundException(
                    "WebAuthn credential with ID %d not found".formatted(credentialId));
        }
        webAuthnCredentialRepository.deleteById(credentialId);
        LOG.info("WebAuthn credential deleted: {}", credentialId);
    }

    private String generateCredentialId() {
        final var random = new SecureRandom();
        final var bytes = new byte[CREDENTIAL_ID_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
