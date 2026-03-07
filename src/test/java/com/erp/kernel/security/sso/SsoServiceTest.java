package com.erp.kernel.security.sso;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link SsoService}.
 */
@ExtendWith(MockitoExtension.class)
class SsoServiceTest {

    @Mock
    private SsoProperties ssoProperties;

    @InjectMocks
    private SsoService ssoService;

    @Test
    void shouldReturnEnabled_whenSsoIsEnabled() {
        when(ssoProperties.isEnabled()).thenReturn(true);

        assertThat(ssoService.isEnabled()).isTrue();
    }

    @Test
    void shouldReturnDisabled_whenSsoIsDisabled() {
        when(ssoProperties.isEnabled()).thenReturn(false);

        assertThat(ssoService.isEnabled()).isFalse();
    }

    @Test
    void shouldReturnProviderType() {
        when(ssoProperties.getProviderType()).thenReturn("SAML");

        assertThat(ssoService.getProviderType()).isEqualTo("SAML");
    }

    @Test
    void shouldThrowException_whenInitiatingAuthWithSsoDisabled() {
        when(ssoProperties.isEnabled()).thenReturn(false);

        assertThatThrownBy(() -> ssoService.initiateAuthentication(SsoProviderType.SAML))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not enabled");
    }

    @Test
    void shouldReturnSamlRedirectUrl_whenSsoEnabled() {
        when(ssoProperties.isEnabled()).thenReturn(true);
        when(ssoProperties.getMetadataUrl()).thenReturn("https://idp.example.com/metadata");

        final var url = ssoService.initiateAuthentication(SsoProviderType.SAML);

        assertThat(url).isEqualTo("https://idp.example.com/metadata");
    }

    @Test
    void shouldReturnOidcRedirectUrl_whenSsoEnabled() {
        when(ssoProperties.isEnabled()).thenReturn(true);
        when(ssoProperties.getIssuerUri()).thenReturn("https://accounts.google.com");

        final var url = ssoService.initiateAuthentication(SsoProviderType.OIDC);

        assertThat(url).isEqualTo("https://accounts.google.com");
    }

    @Test
    void shouldReturnEmptyString_whenMetadataUrlIsNull() {
        when(ssoProperties.isEnabled()).thenReturn(true);
        when(ssoProperties.getMetadataUrl()).thenReturn(null);

        final var url = ssoService.initiateAuthentication(SsoProviderType.SAML);

        assertThat(url).isEmpty();
    }

    @Test
    void shouldReturnEmptyString_whenIssuerUriIsNull() {
        when(ssoProperties.isEnabled()).thenReturn(true);
        when(ssoProperties.getIssuerUri()).thenReturn(null);

        final var url = ssoService.initiateAuthentication(SsoProviderType.OIDC);

        assertThat(url).isEmpty();
    }
}
