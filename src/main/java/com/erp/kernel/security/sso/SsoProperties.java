package com.erp.kernel.security.sso;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Single Sign-On (SSO) integration.
 *
 * <p>Binds to properties prefixed with {@code erp.security.sso}.
 */
@Component
@ConfigurationProperties(prefix = "erp.security.sso")
public class SsoProperties {

    private boolean enabled;
    private String providerType;
    private String issuerUri;
    private String clientId;
    private String clientSecret;
    private String metadataUrl;

    /**
     * Returns whether SSO is enabled.
     *
     * @return {@code true} if enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether SSO is enabled.
     *
     * @param enabled the enabled flag
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns the SSO provider type.
     *
     * @return the provider type (SAML or OIDC)
     */
    public String getProviderType() {
        return providerType;
    }

    /**
     * Sets the SSO provider type.
     *
     * @param providerType the provider type
     */
    public void setProviderType(final String providerType) {
        this.providerType = providerType;
    }

    /**
     * Returns the issuer URI for OIDC.
     *
     * @return the issuer URI
     */
    public String getIssuerUri() {
        return issuerUri;
    }

    /**
     * Sets the issuer URI for OIDC.
     *
     * @param issuerUri the issuer URI
     */
    public void setIssuerUri(final String issuerUri) {
        this.issuerUri = issuerUri;
    }

    /**
     * Returns the client ID.
     *
     * @return the client ID
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the client ID.
     *
     * @param clientId the client ID
     */
    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    /**
     * Returns the client secret.
     *
     * @return the client secret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Sets the client secret.
     *
     * @param clientSecret the client secret
     */
    public void setClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * Returns the SAML metadata URL.
     *
     * @return the metadata URL
     */
    public String getMetadataUrl() {
        return metadataUrl;
    }

    /**
     * Sets the SAML metadata URL.
     *
     * @param metadataUrl the metadata URL
     */
    public void setMetadataUrl(final String metadataUrl) {
        this.metadataUrl = metadataUrl;
    }
}
