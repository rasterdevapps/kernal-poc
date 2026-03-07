package com.erp.kernel.ddic.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ErrorResponse}.
 */
class ErrorResponseTest {

    @Test
    void shouldCreateWithMessage() {
        final var response = new ErrorResponse("Something went wrong");

        assertThat(response.message()).isEqualTo("Something went wrong");
    }

    @Test
    void shouldBeEqualWhenSameMessage() {
        final var response1 = new ErrorResponse("error");
        final var response2 = new ErrorResponse("error");

        assertThat(response1).isEqualTo(response2);
    }

    @Test
    void shouldHaveSameHashCodeWhenSameMessage() {
        final var response1 = new ErrorResponse("error");
        final var response2 = new ErrorResponse("error");

        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    void shouldHaveToString() {
        final var response = new ErrorResponse("error");

        assertThat(response.toString()).contains("error");
    }
}
