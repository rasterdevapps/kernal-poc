package com.erp.kernel.ddic.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ValidationException}.
 */
class ValidationExceptionTest {

    @Test
    void shouldCreateWithMessage() {
        final var message = "Domain name must not be blank";
        final var exception = new ValidationException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldBeRuntimeException() {
        final var exception = new ValidationException("test");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
