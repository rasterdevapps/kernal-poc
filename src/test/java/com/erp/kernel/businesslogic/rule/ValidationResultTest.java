package com.erp.kernel.businesslogic.rule;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ValidationResult}.
 */
class ValidationResultTest {

    @Test
    void shouldCreateSuccessResult() {
        final var result = ValidationResult.success();

        assertThat(result.valid()).isTrue();
        assertThat(result.messages()).isEmpty();
    }

    @Test
    void shouldCreateFailureResult_withMultipleMessages() {
        final var result = ValidationResult.failure(List.of("Error 1", "Error 2"));

        assertThat(result.valid()).isFalse();
        assertThat(result.messages()).hasSize(2);
        assertThat(result.messages()).containsExactly("Error 1", "Error 2");
    }

    @Test
    void shouldCreateFailureResult_withSingleMessage() {
        final var result = ValidationResult.failure("Single error");

        assertThat(result.valid()).isFalse();
        assertThat(result.messages()).hasSize(1);
        assertThat(result.messages()).containsExactly("Single error");
    }

    @Test
    void shouldReturnImmutableMessages_fromFailureWithList() {
        final var messages = new java.util.ArrayList<>(List.of("msg"));
        final var result = ValidationResult.failure(messages);
        messages.add("new msg");

        assertThat(result.messages()).hasSize(1);
    }
}
