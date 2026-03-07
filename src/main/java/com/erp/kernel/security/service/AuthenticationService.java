package com.erp.kernel.security.service;

import com.erp.kernel.security.auth.AuthenticationProvider;
import com.erp.kernel.security.auth.AuthenticationProviderType;
import com.erp.kernel.security.auth.AuthenticationResult;
import com.erp.kernel.security.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Core authentication service implementing the extensible authentication framework.
 *
 * <p>Manages a chain of {@link AuthenticationProvider} implementations and
 * delegates authentication to the appropriate provider based on the requested
 * type. New providers can be added without modifying this service.
 */
@Service
public class AuthenticationService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    private final List<AuthenticationProvider> providers;

    /**
     * Creates a new authentication service.
     *
     * @param providers the list of authentication providers
     */
    public AuthenticationService(final List<AuthenticationProvider> providers) {
        this.providers = Objects.requireNonNull(providers, "providers must not be null");
        LOG.info("Authentication service initialised with {} providers", providers.size());
    }

    /**
     * Authenticates a user using the specified provider type.
     *
     * @param username     the username
     * @param credential   the authentication credential
     * @param providerType the provider type to use
     * @return the authentication result
     * @throws AuthenticationException if no provider supports the type
     */
    public AuthenticationResult authenticate(final String username, final String credential,
                                             final AuthenticationProviderType providerType) {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(credential, "credential must not be null");
        Objects.requireNonNull(providerType, "providerType must not be null");

        final var provider = providers.stream()
                .filter(p -> p.supports(providerType))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException(
                        "No authentication provider found for type: %s".formatted(providerType)));

        LOG.debug("Authenticating user '{}' via {} provider", username, providerType);
        return provider.authenticate(username, credential);
    }

    /**
     * Authenticates using the default provider chain (LOCAL first, then LDAP).
     *
     * @param username   the username
     * @param credential the authentication credential
     * @return the authentication result
     */
    public AuthenticationResult authenticate(final String username, final String credential) {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(credential, "credential must not be null");

        for (final var provider : providers) {
            final var result = provider.authenticate(username, credential);
            if (result.authenticated()) {
                LOG.info("User '{}' authenticated via {} provider", username, provider.getType());
                return result;
            }
        }

        LOG.warn("Authentication failed for user '{}' across all providers", username);
        return AuthenticationResult.failure(username, AuthenticationProviderType.LOCAL,
                "Authentication failed");
    }

    /**
     * Returns the list of supported authentication provider types.
     *
     * @return the list of provider types
     */
    public List<AuthenticationProviderType> getSupportedProviderTypes() {
        return providers.stream()
                .map(AuthenticationProvider::getType)
                .toList();
    }
}
