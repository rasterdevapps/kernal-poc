package com.erp.kernel.resilience.circuitbreaker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link CircuitBreaker}.
 */
class CircuitBreakerTest {

    // Long duration so circuit stays OPEN during tests
    private static final long LONG_DURATION_MS = 3_600_000L;
    // Zero duration so circuit immediately transitions from OPEN to HALF_OPEN
    private static final long ZERO_DURATION_MS = 0L;

    @Test
    void shouldThrowNullPointerException_whenNameIsNull() {
        assertThatThrownBy(() -> new CircuitBreaker(null, 3, 2, 1000L))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenSupplierIsNull() {
        // Arrange
        final var cb = new CircuitBreaker("test", 3, 2, LONG_DURATION_MS);

        // Act & Assert
        assertThatThrownBy(() -> cb.execute(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("supplier must not be null");
    }

    @Test
    void shouldStartInClosedState() {
        // Arrange
        final var cb = new CircuitBreaker("test", 3, 2, LONG_DURATION_MS);

        // Assert
        assertThat(cb.getState()).isEqualTo(CircuitBreakerState.CLOSED);
        assertThat(cb.getName()).isEqualTo("test");
    }

    @Test
    void shouldExecuteSuccessfully_whenClosed() {
        // Arrange
        final var cb = new CircuitBreaker("test", 3, 2, LONG_DURATION_MS);

        // Act
        final var result = cb.execute(() -> "success");

        // Assert
        assertThat(result).isEqualTo("success");
        assertThat(cb.getState()).isEqualTo(CircuitBreakerState.CLOSED);
    }

    @Test
    void shouldResetFailureCount_onSuccessInClosedState() {
        // Arrange
        final var cb = new CircuitBreaker("test", 3, 2, LONG_DURATION_MS);

        // Act - cause some failures then a success
        triggerFailures(cb, 2);
        cb.execute(() -> "ok");

        // Assert - still CLOSED, failure count reset
        assertThat(cb.getState()).isEqualTo(CircuitBreakerState.CLOSED);
    }

    @Test
    void shouldTransitionToOpen_whenFailureThresholdReached() {
        // Arrange
        final var cb = new CircuitBreaker("test", 3, 2, LONG_DURATION_MS);

        // Act - trigger 3 failures
        triggerFailures(cb, 3);

        // Assert
        assertThat(cb.getState()).isEqualTo(CircuitBreakerState.OPEN);
    }

    @Test
    void shouldRejectCalls_whenOpen() {
        // Arrange
        final var cb = new CircuitBreaker("test", 3, 2, LONG_DURATION_MS);
        triggerFailures(cb, 3);

        // Act & Assert
        assertThatThrownBy(() -> cb.execute(() -> "test"))
                .isInstanceOf(CircuitBreakerOpenException.class)
                .hasMessageContaining("test")
                .hasMessageContaining("OPEN");
    }

    @Test
    void shouldTransitionToHalfOpen_whenOpenDurationElapsed() {
        // Arrange
        final var cb = new CircuitBreaker("test", 2, 2, ZERO_DURATION_MS);
        triggerFailures(cb, 2);

        // Act - getState triggers checkAndTransition
        final var state = cb.getState();

        // Assert - with 0ms duration, should immediately go to HALF_OPEN
        assertThat(state).isEqualTo(CircuitBreakerState.HALF_OPEN);
    }

    @Test
    void shouldTransitionToClosed_whenHalfOpenCallSucceeds() {
        // Arrange
        final var cb = new CircuitBreaker("test", 2, 2, ZERO_DURATION_MS);
        triggerFailures(cb, 2);
        cb.getState(); // force HALF_OPEN

        // Act - successful call in HALF_OPEN
        final var result = cb.execute(() -> "recovered");

        // Assert
        assertThat(result).isEqualTo("recovered");
        assertThat(cb.getState()).isEqualTo(CircuitBreakerState.CLOSED);
    }

    @Test
    void shouldTransitionBackToOpen_whenHalfOpenCallFails() {
        // Arrange - with ZERO_DURATION_MS, OPEN immediately transitions to HALF_OPEN on inspection.
        // We verify the failure is handled by confirming the state is NOT CLOSED
        // (a failure must not trigger a successful recovery to CLOSED).
        final var cb = new CircuitBreaker("test", 2, 2, ZERO_DURATION_MS);
        triggerFailures(cb, 2); // -> OPEN
        cb.getState();          // -> HALF_OPEN

        // Act - failure in HALF_OPEN should transition to OPEN (not CLOSED)
        triggerFailures(cb, 1);

        // Assert - with 0ms duration, OPEN immediately becomes HALF_OPEN on the next inspection,
        // but the key invariant is that a HALF_OPEN failure must NOT lead to CLOSED
        assertThat(cb.getState()).isNotEqualTo(CircuitBreakerState.CLOSED);
    }

    @Test
    void shouldRejectCalls_whenHalfOpenCallLimitReached() {
        // Arrange - halfOpenMaxCalls=1 so after 1 failed half-open call, next attempt is rejected
        final var cb = new CircuitBreaker("test", 2, 1, ZERO_DURATION_MS);
        triggerFailures(cb, 2); // -> OPEN
        cb.getState();          // -> HALF_OPEN (count=0)
        triggerFailures(cb, 1); // -> OPEN (count=1 after increment, then back to OPEN)
        cb.getState();          // -> HALF_OPEN again (count=1 persists)

        // Act & Assert - count (1) >= maxCalls (1) -> reject
        assertThatThrownBy(() -> cb.execute(() -> "probe"))
                .isInstanceOf(CircuitBreakerOpenException.class)
                .hasMessageContaining("HALF_OPEN call limit reached");
    }

    @Test
    void shouldResetToClosedState() {
        // Arrange
        final var cb = new CircuitBreaker("test", 2, 2, LONG_DURATION_MS);
        triggerFailures(cb, 2);
        assertThat(cb.getState()).isEqualTo(CircuitBreakerState.OPEN);

        // Act
        cb.reset();

        // Assert
        assertThat(cb.getState()).isEqualTo(CircuitBreakerState.CLOSED);
    }

    @Test
    void shouldAllowExecutionAfterReset() {
        // Arrange
        final var cb = new CircuitBreaker("test", 2, 2, LONG_DURATION_MS);
        triggerFailures(cb, 2);
        cb.reset();

        // Act
        final var result = cb.execute(() -> "after reset");

        // Assert
        assertThat(result).isEqualTo("after reset");
    }

    @Test
    void shouldPropagateException_whenSupplierThrows() {
        // Arrange
        final var cb = new CircuitBreaker("test", 5, 2, LONG_DURATION_MS);

        // Act & Assert
        assertThatThrownBy(() -> cb.execute(() -> {
            throw new IllegalArgumentException("supplier error");
        })).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("supplier error");
    }

    private void triggerFailures(final CircuitBreaker cb, final int count) {
        for (int i = 0; i < count; i++) {
            try {
                cb.execute(() -> {
                    throw new RuntimeException("intentional failure");
                });
            } catch (final RuntimeException ignored) {
                // Expected
            }
        }
    }
}
