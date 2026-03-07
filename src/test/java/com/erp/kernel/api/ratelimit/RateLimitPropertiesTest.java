package com.erp.kernel.api.ratelimit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RateLimitProperties}.
 */
class RateLimitPropertiesTest {

    @Test
    void shouldReturnConfiguredValues_whenCreated() {
        // Arrange
        final var properties = new RateLimitProperties(true, 100, 200);

        // Act & Assert
        assertThat(properties.enabled()).isTrue();
        assertThat(properties.requestsPerSecond()).isEqualTo(100);
        assertThat(properties.burstCapacity()).isEqualTo(200);
    }

    @Test
    void shouldReturnDisabled_whenEnabledIsFalse() {
        // Arrange
        final var properties = new RateLimitProperties(false, 50, 100);

        // Act & Assert
        assertThat(properties.enabled()).isFalse();
        assertThat(properties.requestsPerSecond()).isEqualTo(50);
        assertThat(properties.burstCapacity()).isEqualTo(100);
    }

    @Test
    void shouldSupportEquality_whenSameValues() {
        // Arrange
        final var properties1 = new RateLimitProperties(true, 100, 200);
        final var properties2 = new RateLimitProperties(true, 100, 200);

        // Act & Assert
        assertThat(properties1).isEqualTo(properties2);
        assertThat(properties1.hashCode()).isEqualTo(properties2.hashCode());
    }

    @Test
    void shouldNotBeEqual_whenDifferentValues() {
        // Arrange
        final var properties1 = new RateLimitProperties(true, 100, 200);
        final var properties2 = new RateLimitProperties(false, 50, 100);

        // Act & Assert
        assertThat(properties1).isNotEqualTo(properties2);
    }

    @Test
    void shouldReturnMeaningfulToString_whenCalled() {
        // Arrange
        final var properties = new RateLimitProperties(true, 100, 200);

        // Act
        final var result = properties.toString();

        // Assert
        assertThat(result).contains("true", "100", "200");
    }
}
