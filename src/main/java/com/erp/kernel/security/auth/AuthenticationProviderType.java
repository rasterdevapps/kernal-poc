package com.erp.kernel.security.auth;

/**
 * Enumerates the supported authentication provider types.
 *
 * <p>The extensible authentication framework supports adding new provider
 * types without modifying the core authentication logic.
 */
public enum AuthenticationProviderType {

    /**
     * Local database authentication with username and password.
     */
    LOCAL,

    /**
     * LDAP/Active Directory authentication.
     */
    LDAP,

    /**
     * SAML 2.0 Single Sign-On authentication.
     */
    SAML,

    /**
     * OpenID Connect Single Sign-On authentication.
     */
    OIDC,

    /**
     * WebAuthn/FIDO2 passwordless authentication.
     */
    WEBAUTHN
}
