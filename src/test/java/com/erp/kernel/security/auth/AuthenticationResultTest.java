package com.erp.kernel.security.auth;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link AuthenticationResult}.
 */
class AuthenticationResultTest {

    @Test
    void shouldCreateSuccessResult() {
        final var result = AuthenticationResult.success("user", AuthenticationProviderType.LOCAL);

        assertThat(result.authenticated()).isTrue();
        assertThat(result.username()).isEqualTo("user");
        assertThat(result.providerType()).isEqualTo(AuthenticationProviderType.LOCAL);
        assertThat(result.message()).isEqualTo("Authentication successful");
        assertThat(result.mfaRequired()).isFalse();
    }

    @Test
    void shouldCreateMfaRequiredResult() {
        final var result = AuthenticationResult.mfaRequired("user", AuthenticationProviderType.LOCAL);

        assertThat(result.authenticated()).isTrue();
        assertThat(result.mfaRequired()).isTrue();
        assertThat(result.message()).isEqualTo("MFA verification required");
    }

    @Test
    void shouldCreateFailureResult() {
        final var result = AuthenticationResult.failure("user", AuthenticationProviderType.LDAP, "Bad creds");

        assertThat(result.authenticated()).isFalse();
        assertThat(result.username()).isEqualTo("user");
        assertThat(result.providerType()).isEqualTo(AuthenticationProviderType.LDAP);
        assertThat(result.message()).isEqualTo("Bad creds");
        assertThat(result.mfaRequired()).isFalse();
    }
}
