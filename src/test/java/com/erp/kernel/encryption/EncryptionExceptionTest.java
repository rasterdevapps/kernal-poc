package com.erp.kernel.encryption;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EncryptionException}.
 */
class EncryptionExceptionTest {

    @Test
    void shouldCreateWithMessageAndCause() {
        final var cause = new RuntimeException("root cause");
        final var exception = new EncryptionException("encryption failed", cause);

        assertThat(exception.getMessage()).isEqualTo("encryption failed");
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void shouldBeRuntimeException() {
        final var exception = new EncryptionException("test", new RuntimeException());

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
