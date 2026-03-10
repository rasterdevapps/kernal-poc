package com.erp.kernel.api.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for JWT token generation and validation.
 *
 * @param secretKey         the Base64-encoded secret key for HMAC-SHA256 signing
 * @param expirationMinutes the token expiration time in minutes
 * @param issuer            the token issuer claim
 */
@ConfigurationProperties(prefix = "erp.api.jwt")
public record JwtProperties(
        String secretKey,
        long expirationMinutes,
        String issuer
) {
}
