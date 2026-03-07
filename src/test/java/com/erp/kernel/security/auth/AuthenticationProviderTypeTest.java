package com.erp.kernel.security.auth;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link AuthenticationProviderType} enum.
 */
class AuthenticationProviderTypeTest {

    @Test
    void shouldHaveAllExpectedValues() {
        assertThat(AuthenticationProviderType.values()).containsExactly(
                AuthenticationProviderType.LOCAL,
                AuthenticationProviderType.LDAP,
                AuthenticationProviderType.SAML,
                AuthenticationProviderType.OIDC,
                AuthenticationProviderType.WEBAUTHN
        );
    }

    @Test
    void shouldConvertFromString() {
        assertThat(AuthenticationProviderType.valueOf("LOCAL"))
                .isEqualTo(AuthenticationProviderType.LOCAL);
    }
}
