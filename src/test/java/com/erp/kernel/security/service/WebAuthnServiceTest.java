package com.erp.kernel.security.service;

import com.erp.kernel.security.entity.WebAuthnCredential;
import com.erp.kernel.security.repository.WebAuthnCredentialRepository;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link WebAuthnService}.
 */
@ExtendWith(MockitoExtension.class)
class WebAuthnServiceTest {

    @Mock
    private WebAuthnCredentialRepository webAuthnCredentialRepository;

    @InjectMocks
    private WebAuthnService webAuthnService;

    @Test
    void shouldRegisterCredential() {
        when(webAuthnCredentialRepository.save(any(WebAuthnCredential.class)))
                .thenAnswer(invocation -> {
                    final var saved = invocation.<WebAuthnCredential>getArgument(0);
                    saved.setId(1L);
                    saved.setCreatedAt(Instant.now());
                    saved.setUpdatedAt(Instant.now());
                    return saved;
                });

        final var result = webAuthnService.registerCredential(1L, "publicKey123", "My YubiKey");

        assertThat(result).isNotNull();
        assertThat(result.getPublicKey()).isEqualTo("publicKey123");
        assertThat(result.getCredentialName()).isEqualTo("My YubiKey");
        verify(webAuthnCredentialRepository).save(any(WebAuthnCredential.class));
    }

    @Test
    void shouldGetCredentials() {
        final var cred = createCredential(1L, "cred1");
        when(webAuthnCredentialRepository.findByUserId(1L)).thenReturn(List.of(cred));

        final var result = webAuthnService.getCredentials(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldValidateAssertion_whenSignCountIsHigher() {
        final var cred = createCredential(1L, "cred1");
        cred.setSignCount(5L);
        when(webAuthnCredentialRepository.findByCredentialId("cred1")).thenReturn(Optional.of(cred));
        when(webAuthnCredentialRepository.save(any(WebAuthnCredential.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var result = webAuthnService.validateAssertion("cred1", 6L);

        assertThat(result).isTrue();
        assertThat(cred.getSignCount()).isEqualTo(6L);
    }

    @Test
    void shouldRejectAssertion_whenSignCountIsNotHigher() {
        final var cred = createCredential(1L, "cred1");
        cred.setSignCount(10L);
        when(webAuthnCredentialRepository.findByCredentialId("cred1")).thenReturn(Optional.of(cred));

        final var result = webAuthnService.validateAssertion("cred1", 5L);

        assertThat(result).isFalse();
    }

    @Test
    void shouldRejectAssertion_whenSignCountIsEqual() {
        final var cred = createCredential(1L, "cred1");
        cred.setSignCount(10L);
        when(webAuthnCredentialRepository.findByCredentialId("cred1")).thenReturn(Optional.of(cred));

        final var result = webAuthnService.validateAssertion("cred1", 10L);

        assertThat(result).isFalse();
    }

    @Test
    void shouldThrowEntityNotFoundException_whenAssertionCredentialNotFound() {
        when(webAuthnCredentialRepository.findByCredentialId("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> webAuthnService.validateAssertion("unknown", 1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldDeleteCredential_whenIdExists() {
        when(webAuthnCredentialRepository.existsById(1L)).thenReturn(true);

        webAuthnService.deleteCredential(1L);

        verify(webAuthnCredentialRepository).deleteById(1L);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonExistentCredential() {
        when(webAuthnCredentialRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> webAuthnService.deleteCredential(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private WebAuthnCredential createCredential(final Long userId, final String credentialId) {
        final var cred = new WebAuthnCredential();
        cred.setUserId(userId);
        cred.setCredentialId(credentialId);
        cred.setPublicKey("testPublicKey");
        cred.setCredentialName("Test Key");
        cred.setSignCount(0L);
        cred.setCreatedAt(Instant.now());
        cred.setUpdatedAt(Instant.now());
        return cred;
    }
}
