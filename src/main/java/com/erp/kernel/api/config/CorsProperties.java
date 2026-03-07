package com.erp.kernel.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Configuration properties for Cross-Origin Resource Sharing (CORS).
 *
 * @param allowedOrigins  the allowed origin patterns (e.g., "http://localhost:4200")
 * @param allowedMethods  the allowed HTTP methods
 * @param allowedHeaders  the allowed request headers
 * @param allowCredentials whether credentials are allowed
 * @param maxAge          the maximum age (seconds) for preflight cache
 */
@ConfigurationProperties(prefix = "erp.api.cors")
public record CorsProperties(
        List<String> allowedOrigins,
        List<String> allowedMethods,
        List<String> allowedHeaders,
        boolean allowCredentials,
        long maxAge
) {
}
