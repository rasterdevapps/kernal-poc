package com.erp.kernel.resilience.health;

import com.erp.kernel.resilience.circuitbreaker.CircuitBreaker;
import com.erp.kernel.resilience.circuitbreaker.CircuitBreakerRegistry;
import com.erp.kernel.resilience.circuitbreaker.CircuitBreakerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Status;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ResilienceHealthIndicator}.
 */
@ExtendWith(MockitoExtension.class)
class ResilienceHealthIndicatorTest {

    @Mock
    private CircuitBreakerRegistry registry;

    private ResilienceHealthIndicator indicator;

    @BeforeEach
    void setUp() {
        indicator = new ResilienceHealthIndicator(registry);
    }

    @Test
    void shouldThrowNullPointerException_whenRegistryIsNull() {
        assertThatThrownBy(() -> new ResilienceHealthIndicator(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("circuitBreakerRegistry must not be null");
    }

    @Test
    void shouldReturnUp_whenNoCircuitBreakers() {
        // Arrange
        when(registry.getAll()).thenReturn(List.of());

        // Act
        final var health = indicator.health();

        // Assert
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("total", 0);
    }

    @Test
    void shouldReturnUp_whenAllCircuitBreakersAreClosed() {
        // Arrange
        final var cb1 = mockCircuitBreaker("db", CircuitBreakerState.CLOSED);
        final var cb2 = mockCircuitBreaker("cache", CircuitBreakerState.HALF_OPEN);
        when(registry.getAll()).thenReturn(List.of(cb1, cb2));

        // Act
        final var health = indicator.health();

        // Assert
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("db", "CLOSED");
        assertThat(health.getDetails()).containsEntry("cache", "HALF_OPEN");
        assertThat(health.getDetails()).containsEntry("total", 2);
    }

    @Test
    void shouldReturnDown_whenAnyCircuitBreakerIsOpen() {
        // Arrange
        final var cb1 = mockCircuitBreaker("db", CircuitBreakerState.CLOSED);
        final var cb2 = mockCircuitBreaker("external", CircuitBreakerState.OPEN);
        when(registry.getAll()).thenReturn(List.of(cb1, cb2));

        // Act
        final var health = indicator.health();

        // Assert
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("external", "OPEN");
    }

    private CircuitBreaker mockCircuitBreaker(final String name, final CircuitBreakerState state) {
        final var cb = mock(CircuitBreaker.class);
        when(cb.getName()).thenReturn(name);
        when(cb.getState()).thenReturn(state);
        return cb;
    }
}
