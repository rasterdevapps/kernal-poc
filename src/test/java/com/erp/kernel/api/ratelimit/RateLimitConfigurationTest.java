package com.erp.kernel.api.ratelimit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link RateLimitConfiguration}.
 */
class RateLimitConfigurationTest {

    private final RateLimitConfiguration configuration = new RateLimitConfiguration();

    @Test
    void shouldCreateRateLimitFilter_whenPropertiesProvided() {
        // Arrange
        final var properties = new RateLimitProperties(true, 100, 200);

        // Act
        final var filter = configuration.rateLimitFilter(properties);

        // Assert
        assertThat(filter).isNotNull();
    }

    @Test
    void shouldThrowNullPointerException_whenPropertiesIsNull() {
        assertThatThrownBy(() -> configuration.rateLimitFilter(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("properties must not be null");
    }
}
