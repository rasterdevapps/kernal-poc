package com.erp.kernel.resilience.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Status;
import org.springframework.cache.CacheManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link CacheHealthIndicator}.
 */
@ExtendWith(MockitoExtension.class)
class CacheHealthIndicatorTest {

    @Mock
    private CacheManager cacheManager;

    private CacheHealthIndicator indicator;

    @BeforeEach
    void setUp() {
        indicator = new CacheHealthIndicator(cacheManager);
    }

    @Test
    void shouldThrowNullPointerException_whenCacheManagerIsNull() {
        assertThatThrownBy(() -> new CacheHealthIndicator(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("cacheManager must not be null");
    }

    @Test
    void shouldReturnUp_whenCacheManagerIsAccessible() {
        // Arrange
        when(cacheManager.getCacheNames()).thenReturn(List.of("domains", "users"));

        // Act
        final var health = indicator.health();

        // Assert
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsKey("caches");
        assertThat(health.getDetails()).containsEntry("cacheCount", 2);
    }

    @Test
    void shouldReturnDown_whenCacheManagerThrowsException() {
        // Arrange
        when(cacheManager.getCacheNames()).thenThrow(new RuntimeException("Cache unavailable"));

        // Act
        final var health = indicator.health();

        // Assert
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsKey("error");
    }
}
