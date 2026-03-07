package com.erp.kernel.config;

import org.junit.jupiter.api.Test;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CacheConfig}.
 */
class CacheConfigTest {

    private final CacheConfig cacheConfig = new CacheConfig();

    @Test
    void shouldCreateCacheManager() {
        final var cacheManager = cacheConfig.cacheManager();

        assertThat(cacheManager).isNotNull();
        assertThat(cacheManager).isInstanceOf(CaffeineCacheManager.class);
    }

    @Test
    void shouldContainDomainsCacheName() {
        final var cacheManager = cacheConfig.cacheManager();

        assertThat(cacheManager.getCache("domains")).isNotNull();
    }

    @Test
    void shouldContainDataElementsCacheName() {
        final var cacheManager = cacheConfig.cacheManager();

        assertThat(cacheManager.getCache("dataElements")).isNotNull();
    }

    @Test
    void shouldContainTableDefinitionsCacheName() {
        final var cacheManager = cacheConfig.cacheManager();

        assertThat(cacheManager.getCache("tableDefinitions")).isNotNull();
    }

    @Test
    void shouldContainSearchHelpsCacheName() {
        final var cacheManager = cacheConfig.cacheManager();

        assertThat(cacheManager.getCache("searchHelps")).isNotNull();
    }

    @Test
    void shouldHaveCorrectMaxCacheSize() {
        assertThat(CacheConfig.MAX_CACHE_SIZE).isEqualTo(1000);
    }

    @Test
    void shouldHaveCorrectExpireAfterWrite() {
        assertThat(CacheConfig.EXPIRE_AFTER_WRITE).hasMinutes(30);
    }
}
