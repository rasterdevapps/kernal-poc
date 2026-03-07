package com.erp.kernel.security.repository;

import com.erp.kernel.security.entity.WebAuthnCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link WebAuthnCredential} entity persistence operations.
 */
public interface WebAuthnCredentialRepository extends JpaRepository<WebAuthnCredential, Long> {

    /**
     * Finds all WebAuthn credentials for a user.
     *
     * @param userId the user ID
     * @return the list of credentials
     */
    List<WebAuthnCredential> findByUserId(Long userId);

    /**
     * Finds a credential by its credential ID.
     *
     * @param credentialId the credential ID
     * @return the credential if found
     */
    Optional<WebAuthnCredential> findByCredentialId(String credentialId);
}
