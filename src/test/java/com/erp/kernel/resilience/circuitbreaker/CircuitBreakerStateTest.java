package com.erp.kernel.resilience.circuitbreaker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CircuitBreakerState}.
 */
class CircuitBreakerStateTest {

    @Test
    void shouldHaveAllValues() {
        assertThat(CircuitBreakerState.values()).containsExactlyInAnyOrder(
                CircuitBreakerState.CLOSED,
                CircuitBreakerState.OPEN,
                CircuitBreakerState.HALF_OPEN
        );
    }

    @Test
    void shouldParseFromString() {
        assertThat(CircuitBreakerState.valueOf("CLOSED")).isEqualTo(CircuitBreakerState.CLOSED);
        assertThat(CircuitBreakerState.valueOf("OPEN")).isEqualTo(CircuitBreakerState.OPEN);
        assertThat(CircuitBreakerState.valueOf("HALF_OPEN")).isEqualTo(CircuitBreakerState.HALF_OPEN);
    }
}
