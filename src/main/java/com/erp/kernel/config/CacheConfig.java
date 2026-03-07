package com.erp.kernel.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configures Caffeine-based in-memory caching for frequently accessed master data.
 *
 * <p>Provides caches for DDIC entities (domains, data elements, table definitions, search helps)
 * with a maximum size of 1000 entries and 30-minute expiration after write.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /** Maximum number of entries per cache. */
    static final long MAX_CACHE_SIZE = 1000;

    /** Duration after which cache entries expire. */
    static final Duration EXPIRE_AFTER_WRITE = Duration.ofMinutes(30);

    /**
     * Creates and configures the Caffeine cache manager.
     *
     * @return the configured {@link CacheManager}
     */
    @Bean
    public CacheManager cacheManager() {
        final var cacheManager = new CaffeineCacheManager(
                "domains", "dataElements", "tableDefinitions", "searchHelps");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(MAX_CACHE_SIZE)
                .expireAfterWrite(EXPIRE_AFTER_WRITE)
                .recordStats());
        return cacheManager;
    }
}
