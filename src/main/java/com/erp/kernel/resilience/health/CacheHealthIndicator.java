package com.erp.kernel.resilience.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Custom Spring Boot health indicator for the in-memory cache.
 *
 * <p>Verifies that the cache manager is accessible and reports the names of all
 * configured caches in the health details.
 */
@Component("cacheHealth")
public class CacheHealthIndicator implements HealthIndicator {

    private final CacheManager cacheManager;

    /**
     * Creates a new cache health indicator.
     *
     * @param cacheManager the application cache manager
     */
    public CacheHealthIndicator(final CacheManager cacheManager) {
        this.cacheManager = Objects.requireNonNull(
                cacheManager, "cacheManager must not be null");
    }

    /**
     * Checks cache health by verifying the cache manager is accessible.
     *
     * @return {@link Health#up()} with cache names if accessible,
     *         {@link Health#down()} with error details otherwise
     */
    @Override
    public Health health() {
        try {
            final var cacheNames = cacheManager.getCacheNames();
            return Health.up()
                    .withDetail("caches", cacheNames)
                    .withDetail("cacheCount", cacheNames.size())
                    .build();
        } catch (final Exception ex) {
            return Health.down()
                    .withDetail("error", ex.getMessage())
                    .build();
        }
    }
}
