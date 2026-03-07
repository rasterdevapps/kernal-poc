package com.erp.kernel.ddic.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EntityNotFoundException}.
 */
class EntityNotFoundExceptionTest {

    @Test
    void shouldCreateWithMessage() {
        final var message = "Domain not found with id: 42";
        final var exception = new EntityNotFoundException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldBeRuntimeException() {
        final var exception = new EntityNotFoundException("test");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
