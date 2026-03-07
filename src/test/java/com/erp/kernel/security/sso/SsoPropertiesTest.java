package com.erp.kernel.security.sso;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link SsoProperties}.
 */
class SsoPropertiesTest {

    @Test
    void shouldGetAndSetAllProperties() {
        final var props = new SsoProperties();

        props.setEnabled(true);
        props.setProviderType("OIDC");
        props.setIssuerUri("https://issuer.example.com");
        props.setClientId("client-id");
        props.setClientSecret("client-secret");
        props.setMetadataUrl("https://metadata.example.com");

        assertThat(props.isEnabled()).isTrue();
        assertThat(props.getProviderType()).isEqualTo("OIDC");
        assertThat(props.getIssuerUri()).isEqualTo("https://issuer.example.com");
        assertThat(props.getClientId()).isEqualTo("client-id");
        assertThat(props.getClientSecret()).isEqualTo("client-secret");
        assertThat(props.getMetadataUrl()).isEqualTo("https://metadata.example.com");
    }

    @Test
    void shouldDefaultToDisabled() {
        final var props = new SsoProperties();

        assertThat(props.isEnabled()).isFalse();
    }
}
