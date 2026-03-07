package com.erp.kernel.security.sso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service managing Single Sign-On (SSO) operations.
 *
 * <p>Supports both SAML 2.0 and OpenID Connect protocols for enabling
 * users to authenticate once and access multiple system components.
 */
@Service
public class SsoService {

    private static final Logger LOG = LoggerFactory.getLogger(SsoService.class);

    private final SsoProperties ssoProperties;

    /**
     * Creates a new SSO service.
     *
     * @param ssoProperties the SSO configuration properties
     */
    public SsoService(final SsoProperties ssoProperties) {
        this.ssoProperties = Objects.requireNonNull(ssoProperties, "ssoProperties must not be null");
    }

    /**
     * Returns whether SSO is enabled.
     *
     * @return {@code true} if SSO is enabled
     */
    public boolean isEnabled() {
        return ssoProperties.isEnabled();
    }

    /**
     * Returns the configured SSO provider type.
     *
     * @return the provider type as a string, or {@code null} if not configured
     */
    public String getProviderType() {
        return ssoProperties.getProviderType();
    }

    /**
     * Initiates an SSO authentication flow.
     *
     * @param providerType the SSO provider type to use
     * @return the redirect URL for the SSO provider
     * @throws IllegalStateException if SSO is not enabled
     */
    public String initiateAuthentication(final SsoProviderType providerType) {
        Objects.requireNonNull(providerType, "providerType must not be null");

        if (!ssoProperties.isEnabled()) {
            throw new IllegalStateException("SSO is not enabled");
        }

        LOG.info("Initiating {} SSO authentication flow", providerType);

        // The SSO redirect URL would be constructed here based on the provider
        // configuration. For the PoC, the framework is established and ready
        // for integration with external identity providers.
        return switch (providerType) {
            case SAML -> buildSamlRedirectUrl();
            case OIDC -> buildOidcRedirectUrl();
        };
    }

    private String buildSamlRedirectUrl() {
        final var metadataUrl = ssoProperties.getMetadataUrl();
        LOG.debug("Building SAML redirect URL from metadata: {}", metadataUrl);
        return metadataUrl != null ? metadataUrl : "";
    }

    private String buildOidcRedirectUrl() {
        final var issuerUri = ssoProperties.getIssuerUri();
        LOG.debug("Building OIDC redirect URL from issuer: {}", issuerUri);
        return issuerUri != null ? issuerUri : "";
    }
}
