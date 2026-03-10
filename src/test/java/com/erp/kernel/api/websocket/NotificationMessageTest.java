package com.erp.kernel.api.websocket;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link NotificationMessage}.
 */
class NotificationMessageTest {

    @Test
    void shouldCreateBroadcastNotification_whenCallingBroadcast() {
        // Arrange
        final var before = Instant.now();

        // Act
        final var message = NotificationMessage.broadcast("INFO", "Test Title", "Test Content");

        // Assert
        final var after = Instant.now();
        assertThat(message.id()).isNotNull().isNotEmpty();
        assertThat(message.type()).isEqualTo("INFO");
        assertThat(message.title()).isEqualTo("Test Title");
        assertThat(message.content()).isEqualTo("Test Content");
        assertThat(message.recipient()).isNull();
        assertThat(message.timestamp()).isNotNull();
        assertThat(message.timestamp()).isBetween(before, after);
    }

    @Test
    void shouldCreateTargetedNotification_whenCallingTargeted() {
        // Arrange
        final var before = Instant.now();

        // Act
        final var message = NotificationMessage.targeted(
                "WARNING", "Alert", "Something happened", "admin");

        // Assert
        final var after = Instant.now();
        assertThat(message.id()).isNotNull().isNotEmpty();
        assertThat(message.type()).isEqualTo("WARNING");
        assertThat(message.title()).isEqualTo("Alert");
        assertThat(message.content()).isEqualTo("Something happened");
        assertThat(message.recipient()).isEqualTo("admin");
        assertThat(message.timestamp()).isNotNull();
        assertThat(message.timestamp()).isBetween(before, after);
    }

    @Test
    void shouldGenerateUniqueIds_whenCreatingMultipleMessages() {
        // Act
        final var message1 = NotificationMessage.broadcast("INFO", "Title1", "Content1");
        final var message2 = NotificationMessage.broadcast("INFO", "Title2", "Content2");

        // Assert
        assertThat(message1.id()).isNotEqualTo(message2.id());
    }

    @Test
    void shouldCreateRecordWithAllFields_whenUsingConstructor() {
        // Arrange
        final var timestamp = Instant.now();

        // Act
        final var message = new NotificationMessage(
                "id-123", "ERROR", "Error Title", "Error Content", "user1", timestamp);

        // Assert
        assertThat(message.id()).isEqualTo("id-123");
        assertThat(message.type()).isEqualTo("ERROR");
        assertThat(message.title()).isEqualTo("Error Title");
        assertThat(message.content()).isEqualTo("Error Content");
        assertThat(message.recipient()).isEqualTo("user1");
        assertThat(message.timestamp()).isEqualTo(timestamp);
    }

    @Test
    void shouldSupportEquality_whenRecordsHaveSameValues() {
        // Arrange
        final var timestamp = Instant.parse("2025-01-01T00:00:00Z");
        final var message1 = new NotificationMessage(
                "id-1", "INFO", "Title", "Content", null, timestamp);
        final var message2 = new NotificationMessage(
                "id-1", "INFO", "Title", "Content", null, timestamp);

        // Assert
        assertThat(message1).isEqualTo(message2);
        assertThat(message1.hashCode()).isEqualTo(message2.hashCode());
    }

    @Test
    void shouldReturnMeaningfulToString_whenCallingToString() {
        // Arrange
        final var timestamp = Instant.parse("2025-01-01T00:00:00Z");
        final var message = new NotificationMessage(
                "id-1", "INFO", "Title", "Content", "user1", timestamp);

        // Act
        final var result = message.toString();

        // Assert
        assertThat(result).contains("id-1", "INFO", "Title", "Content", "user1");
    }
}
