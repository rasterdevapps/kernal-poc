package com.erp.kernel.api.websocket;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link NotificationController}.
 */
class NotificationControllerTest {

    private final NotificationController controller = new NotificationController();

    @Test
    void shouldReturnSameMessage_whenHandlingNotification() {
        // Arrange
        final var message = new NotificationMessage(
                "id-1", "INFO", "Title", "Content", null, Instant.now());

        // Act
        final var result = controller.handleNotification(message);

        // Assert
        assertThat(result).isEqualTo(message);
        assertThat(result.id()).isEqualTo("id-1");
        assertThat(result.type()).isEqualTo("INFO");
        assertThat(result.title()).isEqualTo("Title");
        assertThat(result.content()).isEqualTo("Content");
        assertThat(result.recipient()).isNull();
    }

    @Test
    void shouldThrowNullPointerException_whenMessageIsNull() {
        // Act & Assert
        assertThatThrownBy(() -> controller.handleNotification(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("message must not be null");
    }

    @Test
    void shouldReturnTargetedMessage_whenHandlingTargetedNotification() {
        // Arrange
        final var message = new NotificationMessage(
                "id-2", "ERROR", "Error Title", "Error Content", "user1", Instant.now());

        // Act
        final var result = controller.handleNotification(message);

        // Assert
        assertThat(result).isEqualTo(message);
        assertThat(result.recipient()).isEqualTo("user1");
    }
}
