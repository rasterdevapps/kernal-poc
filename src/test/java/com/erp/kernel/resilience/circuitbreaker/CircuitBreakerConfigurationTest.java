package com.erp.kernel.resilience.circuitbreaker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link CircuitBreakerConfiguration}.
 */
class CircuitBreakerConfigurationTest {

    private final CircuitBreakerConfiguration configuration = new CircuitBreakerConfiguration();

    @Test
    void shouldCreateCircuitBreakerRegistry_whenPropertiesProvided() {
        // Arrange
        final var properties = new CircuitBreakerProperties(5, 3, 60L);

        // Act
        final var registry = configuration.circuitBreakerRegistry(properties);

        // Assert
        assertThat(registry).isNotNull();
    }

    @Test
    void shouldThrowNullPointerException_whenPropertiesIsNull() {
        assertThatThrownBy(() -> configuration.circuitBreakerRegistry(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("properties must not be null");
    }
}
