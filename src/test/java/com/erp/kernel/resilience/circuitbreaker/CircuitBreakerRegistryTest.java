package com.erp.kernel.resilience.circuitbreaker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link CircuitBreakerRegistry}.
 */
class CircuitBreakerRegistryTest {

    private CircuitBreakerRegistry registry;

    @BeforeEach
    void setUp() {
        final var properties = new CircuitBreakerProperties(5, 3, 60L);
        registry = new CircuitBreakerRegistry(properties);
    }

    @Test
    void shouldThrowNullPointerException_whenPropertiesIsNull() {
        assertThatThrownBy(() -> new CircuitBreakerRegistry(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("properties must not be null");
    }

    @Test
    void shouldCreateCircuitBreaker_whenNameIsNew() {
        // Act
        final var cb = registry.getOrCreate("database");

        // Assert
        assertThat(cb).isNotNull();
        assertThat(cb.getName()).isEqualTo("database");
        assertThat(cb.getState()).isEqualTo(CircuitBreakerState.CLOSED);
    }

    @Test
    void shouldReturnSameInstance_whenNameAlreadyExists() {
        // Act
        final var cb1 = registry.getOrCreate("payment");
        final var cb2 = registry.getOrCreate("payment");

        // Assert
        assertThat(cb1).isSameAs(cb2);
    }

    @Test
    void shouldReturnAllCircuitBreakers() {
        // Arrange
        registry.getOrCreate("db");
        registry.getOrCreate("cache");

        // Act
        final var all = registry.getAll();

        // Assert
        assertThat(all).hasSize(2);
    }

    @Test
    void shouldReturnUnmodifiableCollection() {
        // Arrange
        registry.getOrCreate("db");

        // Act
        final var all = registry.getAll();

        // Assert
        assertThatThrownBy(() -> all.clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenNameIsNull() {
        assertThatThrownBy(() -> registry.getOrCreate(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }
}
