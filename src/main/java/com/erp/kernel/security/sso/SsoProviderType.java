package com.erp.kernel.security.sso;

/**
 * Enumerates the supported SSO provider types.
 *
 * <p>Defines the single sign-on protocols available for integration
 * with external identity providers.
 */
public enum SsoProviderType {

    /**
     * SAML 2.0 based SSO.
     */
    SAML,

    /**
     * OpenID Connect based SSO.
     */
    OIDC
}
