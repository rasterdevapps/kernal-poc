package com.erp.kernel.resilience.circuitbreaker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link CircuitBreakerOpenException}.
 */
class CircuitBreakerOpenExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        // Arrange & Act
        final var exception = new CircuitBreakerOpenException("circuit 'db' is OPEN");

        // Assert
        assertThat(exception.getMessage()).isEqualTo("circuit 'db' is OPEN");
    }

    @Test
    void shouldBeRuntimeException() {
        // Assert
        assertThat(new CircuitBreakerOpenException("test"))
                .isInstanceOf(RuntimeException.class);
    }
}
