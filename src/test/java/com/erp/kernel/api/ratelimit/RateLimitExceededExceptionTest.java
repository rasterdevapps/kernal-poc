package com.erp.kernel.api.ratelimit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RateLimitExceededException}.
 */
class RateLimitExceededExceptionTest {

    @Test
    void shouldContainMessage_whenCreatedWithMessage() {
        // Arrange
        final var message = "Rate limit exceeded for client 192.168.1.1";

        // Act
        final var exception = new RateLimitExceededException(message);

        // Assert
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldBeRuntimeException_whenCreated() {
        // Arrange & Act
        final var exception = new RateLimitExceededException("Too many requests");

        // Assert
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
