package com.erp.kernel.ddic.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DuplicateEntityException}.
 */
class DuplicateEntityExceptionTest {

    @Test
    void shouldCreateWithMessage() {
        final var message = "Domain already exists: TEST_DOMAIN";
        final var exception = new DuplicateEntityException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldBeRuntimeException() {
        final var exception = new DuplicateEntityException("test");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
