package com.erp.kernel.security.service;

import com.erp.kernel.security.auth.AuthenticationProvider;
import com.erp.kernel.security.auth.AuthenticationProviderType;
import com.erp.kernel.security.auth.AuthenticationResult;
import com.erp.kernel.security.exception.AuthenticationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link AuthenticationService}.
 */
class AuthenticationServiceTest {

    @Test
    void shouldAuthenticateWithSpecificProvider() {
        final var provider = createProvider(AuthenticationProviderType.LOCAL, true);
        final var service = new AuthenticationService(List.of(provider));

        final var result = service.authenticate("user", "pass", AuthenticationProviderType.LOCAL);

        assertThat(result.authenticated()).isTrue();
        assertThat(result.providerType()).isEqualTo(AuthenticationProviderType.LOCAL);
    }

    @Test
    void shouldThrowException_whenNoProviderSupportsType() {
        final var provider = createProvider(AuthenticationProviderType.LOCAL, true);
        final var service = new AuthenticationService(List.of(provider));

        assertThatThrownBy(() -> service.authenticate("user", "pass", AuthenticationProviderType.LDAP))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("LDAP");
    }

    @Test
    void shouldAuthenticateWithDefaultChain_whenFirstProviderSucceeds() {
        final var provider = createProvider(AuthenticationProviderType.LOCAL, true);
        final var service = new AuthenticationService(List.of(provider));

        final var result = service.authenticate("user", "pass");

        assertThat(result.authenticated()).isTrue();
    }

    @Test
    void shouldFailAuthentication_whenAllProvidersReject() {
        final var provider = createProvider(AuthenticationProviderType.LOCAL, false);
        final var service = new AuthenticationService(List.of(provider));

        final var result = service.authenticate("user", "wrongpass");

        assertThat(result.authenticated()).isFalse();
    }

    @Test
    void shouldAuthenticateWithSecondProvider_whenFirstFails() {
        final var localProvider = createProvider(AuthenticationProviderType.LOCAL, false);
        final var ldapProvider = createProvider(AuthenticationProviderType.LDAP, true);
        final var service = new AuthenticationService(List.of(localProvider, ldapProvider));

        final var result = service.authenticate("user", "pass");

        assertThat(result.authenticated()).isTrue();
        assertThat(result.providerType()).isEqualTo(AuthenticationProviderType.LDAP);
    }

    @Test
    void shouldReturnSupportedProviderTypes() {
        final var localProvider = createProvider(AuthenticationProviderType.LOCAL, true);
        final var ldapProvider = createProvider(AuthenticationProviderType.LDAP, false);
        final var service = new AuthenticationService(List.of(localProvider, ldapProvider));

        final var types = service.getSupportedProviderTypes();

        assertThat(types).containsExactly(AuthenticationProviderType.LOCAL, AuthenticationProviderType.LDAP);
    }

    private AuthenticationProvider createProvider(final AuthenticationProviderType type,
                                                   final boolean succeeds) {
        return new AuthenticationProvider() {
            @Override
            public AuthenticationProviderType getType() {
                return type;
            }

            @Override
            public AuthenticationResult authenticate(final String username, final String credential) {
                if (succeeds) {
                    return AuthenticationResult.success(username, type);
                }
                return AuthenticationResult.failure(username, type, "Failed");
            }

            @Override
            public boolean supports(final AuthenticationProviderType providerType) {
                return type == providerType;
            }
        };
    }
}
