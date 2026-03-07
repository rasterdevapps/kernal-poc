package com.erp.kernel.security.sso;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link SsoProviderType} enum.
 */
class SsoProviderTypeTest {

    @Test
    void shouldHaveExpectedValues() {
        assertThat(SsoProviderType.values()).containsExactly(
                SsoProviderType.SAML,
                SsoProviderType.OIDC
        );
    }

    @Test
    void shouldConvertFromString() {
        assertThat(SsoProviderType.valueOf("SAML")).isEqualTo(SsoProviderType.SAML);
        assertThat(SsoProviderType.valueOf("OIDC")).isEqualTo(SsoProviderType.OIDC);
    }
}
