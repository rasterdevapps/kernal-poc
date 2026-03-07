package com.erp.kernel.security.auth;

/**
 * Interface for pluggable authentication providers.
 *
 * <p>Part of the extensible authentication framework (Milestone 4.7) that
 * allows new authentication methods to be added without modifying core code.
 * Implementations handle specific authentication mechanisms such as local
 * credentials, LDAP, SSO, or WebAuthn.
 */
public interface AuthenticationProvider {

    /**
     * Returns the type of this authentication provider.
     *
     * @return the provider type
     */
    AuthenticationProviderType getType();

    /**
     * Attempts to authenticate a user with the given credentials.
     *
     * @param username the username
     * @param credential the authentication credential (password, token, etc.)
     * @return the authentication result
     */
    AuthenticationResult authenticate(String username, String credential);

    /**
     * Returns whether this provider supports the given authentication type.
     *
     * @param type the authentication provider type
     * @return {@code true} if this provider supports the type
     */
    boolean supports(AuthenticationProviderType type);
}
