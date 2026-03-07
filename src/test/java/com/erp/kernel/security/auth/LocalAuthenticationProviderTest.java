package com.erp.kernel.security.auth;

import com.erp.kernel.security.entity.User;
import com.erp.kernel.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link LocalAuthenticationProvider}.
 */
@ExtendWith(MockitoExtension.class)
class LocalAuthenticationProviderTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LocalAuthenticationProvider provider;

    @Test
    void shouldReturnLocalType() {
        assertThat(provider.getType()).isEqualTo(AuthenticationProviderType.LOCAL);
    }

    @Test
    void shouldSupportLocalType() {
        assertThat(provider.supports(AuthenticationProviderType.LOCAL)).isTrue();
    }

    @Test
    void shouldNotSupportLdapType() {
        assertThat(provider.supports(AuthenticationProviderType.LDAP)).isFalse();
    }

    @Test
    void shouldAuthenticateSuccessfully_whenPasswordIsCorrect() {
        final var user = new User();
        user.setUsername("jdoe");
        user.setPasswordHash(LocalAuthenticationProvider.hashPassword("secret123"));
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(user));

        final var result = provider.authenticate("jdoe", "secret123");

        assertThat(result.authenticated()).isTrue();
        assertThat(result.username()).isEqualTo("jdoe");
    }

    @Test
    void shouldFailAuthentication_whenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        final var result = provider.authenticate("unknown", "password");

        assertThat(result.authenticated()).isFalse();
        assertThat(result.message()).isEqualTo("Invalid credentials");
    }

    @Test
    void shouldFailAuthentication_whenPasswordIsWrong() {
        final var user = new User();
        user.setUsername("jdoe");
        user.setPasswordHash(LocalAuthenticationProvider.hashPassword("correctpassword"));
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(user));

        final var result = provider.authenticate("jdoe", "wrongpassword");

        assertThat(result.authenticated()).isFalse();
        assertThat(result.message()).isEqualTo("Invalid credentials");
    }

    @Test
    void shouldHashAndVerifyPassword() {
        final var hash = LocalAuthenticationProvider.hashPassword("mypassword");

        assertThat(hash).contains(":");
        assertThat(LocalAuthenticationProvider.verifyPassword("mypassword", hash)).isTrue();
        assertThat(LocalAuthenticationProvider.verifyPassword("wrongpassword", hash)).isFalse();
    }

    @Test
    void shouldRejectVerification_whenHashIsNull() {
        assertThat(LocalAuthenticationProvider.verifyPassword("password", null)).isFalse();
    }

    @Test
    void shouldRejectVerification_whenHashHasNoColon() {
        assertThat(LocalAuthenticationProvider.verifyPassword("password", "nocolon")).isFalse();
    }

    @Test
    void shouldGenerateUniqueHashes() {
        final var hash1 = LocalAuthenticationProvider.hashPassword("samepassword");
        final var hash2 = LocalAuthenticationProvider.hashPassword("samepassword");

        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void shouldThrowAuthenticationException_whenHashAlgorithmNotAvailable() {
        try (var digestMock = org.mockito.Mockito.mockStatic(java.security.MessageDigest.class)) {
            digestMock.when(() -> java.security.MessageDigest.getInstance("SHA-256"))
                    .thenThrow(new java.security.NoSuchAlgorithmException("mocked"));
            digestMock.when(() -> java.security.MessageDigest.isEqual(
                    org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
                    .thenCallRealMethod();

            assertThatThrownBy(() -> LocalAuthenticationProvider.hashPassword("password"))
                    .isInstanceOf(com.erp.kernel.security.exception.AuthenticationException.class)
                    .hasMessageContaining("Hash algorithm not available");
        }
    }
}
