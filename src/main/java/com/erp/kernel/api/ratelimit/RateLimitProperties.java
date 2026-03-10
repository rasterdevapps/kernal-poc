package com.erp.kernel.api.ratelimit;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for API rate limiting.
 *
 * @param enabled           whether rate limiting is enabled
 * @param requestsPerSecond the maximum number of requests allowed per second per client
 * @param burstCapacity     the maximum burst capacity (tokens in the bucket)
 */
@ConfigurationProperties(prefix = "erp.api.rate-limit")
public record RateLimitProperties(
        boolean enabled,
        int requestsPerSecond,
        int burstCapacity
) {
}
