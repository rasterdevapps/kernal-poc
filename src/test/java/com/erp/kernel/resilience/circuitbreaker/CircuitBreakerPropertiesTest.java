package com.erp.kernel.resilience.circuitbreaker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CircuitBreakerProperties}.
 */
class CircuitBreakerPropertiesTest {

    @Test
    void shouldCreatePropertiesRecord() {
        // Arrange & Act
        final var properties = new CircuitBreakerProperties(5, 3, 60L);

        // Assert
        assertThat(properties.failureThreshold()).isEqualTo(5);
        assertThat(properties.halfOpenMaxCalls()).isEqualTo(3);
        assertThat(properties.openStateDurationSeconds()).isEqualTo(60L);
    }

    @Test
    void shouldSupportEquality() {
        // Arrange
        final var p1 = new CircuitBreakerProperties(5, 3, 60L);
        final var p2 = new CircuitBreakerProperties(5, 3, 60L);

        // Assert
        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }
}
