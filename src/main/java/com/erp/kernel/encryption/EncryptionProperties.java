package com.erp.kernel.encryption;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Configuration properties for the AES-256 encryption service.
 *
 * <p>Binds to the {@code erp.encryption} prefix in application configuration.
 */
@Component
@ConfigurationProperties(prefix = "erp.encryption")
public class EncryptionProperties {

    private String secretKey;

    /**
     * Returns the Base64-encoded AES-256 secret key.
     *
     * @return the Base64-encoded secret key
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Sets the Base64-encoded AES-256 secret key.
     *
     * @param secretKey the Base64-encoded secret key (must decode to 32 bytes)
     */
    public void setSecretKey(final String secretKey) {
        this.secretKey = Objects.requireNonNull(secretKey, "secretKey must not be null");
    }
}
