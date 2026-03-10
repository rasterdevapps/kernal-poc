package com.erp.kernel.api.ratelimit;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * Configuration class that registers rate limiting components.
 *
 * <p>Enables {@link RateLimitProperties} binding and creates the
 * {@link RateLimitFilter} servlet filter bean.
 */
@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitConfiguration {

    /**
     * Creates the rate limit filter bean.
     *
     * @param properties the rate limit configuration properties
     * @return the rate limit filter
     */
    @Bean
    public RateLimitFilter rateLimitFilter(final RateLimitProperties properties) {
        Objects.requireNonNull(properties, "properties must not be null");
        return new RateLimitFilter(properties);
    }
}
