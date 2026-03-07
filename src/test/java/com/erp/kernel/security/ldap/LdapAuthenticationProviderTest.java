package com.erp.kernel.security.ldap;

import com.erp.kernel.security.auth.AuthenticationProviderType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link LdapAuthenticationProvider}.
 */
@ExtendWith(MockitoExtension.class)
class LdapAuthenticationProviderTest {

    @Mock
    private LdapProperties ldapProperties;

    @InjectMocks
    private LdapAuthenticationProvider provider;

    @Test
    void shouldReturnLdapType() {
        assertThat(provider.getType()).isEqualTo(AuthenticationProviderType.LDAP);
    }

    @Test
    void shouldSupportLdapType() {
        assertThat(provider.supports(AuthenticationProviderType.LDAP)).isTrue();
    }

    @Test
    void shouldNotSupportLocalType() {
        assertThat(provider.supports(AuthenticationProviderType.LOCAL)).isFalse();
    }

    @Test
    void shouldFailAuthentication_whenLdapIsDisabled() {
        when(ldapProperties.isEnabled()).thenReturn(false);

        final var result = provider.authenticate("user", "pass");

        assertThat(result.authenticated()).isFalse();
        assertThat(result.message()).contains("not enabled");
    }

    @Test
    void shouldReturnFailure_whenLdapIsEnabled() {
        when(ldapProperties.isEnabled()).thenReturn(true);
        when(ldapProperties.getUrl()).thenReturn("ldap://localhost:389");

        final var result = provider.authenticate("user", "pass");

        assertThat(result.authenticated()).isFalse();
        assertThat(result.message()).contains("not configured");
    }
}
