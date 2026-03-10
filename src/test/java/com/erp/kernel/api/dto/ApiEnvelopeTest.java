package com.erp.kernel.api.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ApiEnvelope}.
 */
class ApiEnvelopeTest {

    @Test
    void shouldReturnStatus200AndSuccessMessage_whenOkCalled() {
        // Arrange
        final var data = "test-payload";
        final var before = Instant.now();

        // Act
        final var envelope = ApiEnvelope.ok(data);

        // Assert
        assertThat(envelope.status()).isEqualTo(200);
        assertThat(envelope.message()).isEqualTo("Success");
        assertThat(envelope.data()).isEqualTo("test-payload");
        assertThat(envelope.timestamp()).isAfterOrEqualTo(before);
        assertThat(envelope.timestamp()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    void shouldReturnStatus201AndCreatedMessage_whenCreatedCalled() {
        // Arrange
        final var data = 42;
        final var before = Instant.now();

        // Act
        final var envelope = ApiEnvelope.created(data);

        // Assert
        assertThat(envelope.status()).isEqualTo(201);
        assertThat(envelope.message()).isEqualTo("Created");
        assertThat(envelope.data()).isEqualTo(42);
        assertThat(envelope.timestamp()).isAfterOrEqualTo(before);
    }

    @Test
    void shouldReturnGivenStatusAndNullData_whenErrorCalled() {
        // Arrange
        final var before = Instant.now();

        // Act
        final var envelope = ApiEnvelope.error(404, "Not Found");

        // Assert
        assertThat(envelope.status()).isEqualTo(404);
        assertThat(envelope.message()).isEqualTo("Not Found");
        assertThat(envelope.data()).isNull();
        assertThat(envelope.timestamp()).isAfterOrEqualTo(before);
    }

    @Test
    void shouldReturnNullData_whenOkCalledWithNull() {
        // Act
        final var envelope = ApiEnvelope.ok(null);

        // Assert
        assertThat(envelope.status()).isEqualTo(200);
        assertThat(envelope.data()).isNull();
    }

    @Test
    void shouldSupportGenericTypes_whenCreatedWithComplexType() {
        // Arrange
        final var data = java.util.List.of("a", "b", "c");

        // Act
        final var envelope = ApiEnvelope.ok(data);

        // Assert
        assertThat(envelope.data()).hasSize(3);
        assertThat(envelope.data()).containsExactly("a", "b", "c");
    }

    @Test
    void shouldBeEqual_whenSameValues() {
        // Arrange
        final var timestamp = Instant.parse("2025-01-01T00:00:00Z");
        final var envelope1 = new ApiEnvelope<>(200, "Success", "data", timestamp);
        final var envelope2 = new ApiEnvelope<>(200, "Success", "data", timestamp);

        // Assert
        assertThat(envelope1).isEqualTo(envelope2);
        assertThat(envelope1.hashCode()).isEqualTo(envelope2.hashCode());
    }

    @Test
    void shouldReturnReadableString_whenToStringCalled() {
        // Arrange
        final var envelope = ApiEnvelope.ok("test");

        // Act
        final var result = envelope.toString();

        // Assert
        assertThat(result).contains("ApiEnvelope");
        assertThat(result).contains("200");
        assertThat(result).contains("test");
    }
}
