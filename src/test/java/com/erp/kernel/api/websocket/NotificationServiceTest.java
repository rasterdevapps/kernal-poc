package com.erp.kernel.api.websocket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link NotificationService}.
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldThrowNullPointerException_whenConstructorReceivesNullTemplate() {
        // Act & Assert
        assertThatThrownBy(() -> new NotificationService(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("messagingTemplate must not be null");
    }

    @Test
    void shouldBroadcastMessage_whenCallingBroadcast() {
        // Arrange
        final var message = new NotificationMessage(
                "id-1", "INFO", "Title", "Content", null, Instant.now());

        // Act
        notificationService.broadcast(message);

        // Assert
        verify(messagingTemplate).convertAndSend(
                NotificationService.NOTIFICATIONS_TOPIC, message);
    }

    @Test
    void shouldThrowNullPointerException_whenBroadcastReceivesNullMessage() {
        // Act & Assert
        assertThatThrownBy(() -> notificationService.broadcast(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("message must not be null");
    }

    @Test
    void shouldSendToUser_whenCallingSendToUser() {
        // Arrange
        final var message = new NotificationMessage(
                "id-2", "WARNING", "Alert", "Details", "admin", Instant.now());

        // Act
        notificationService.sendToUser("admin", message);

        // Assert
        verify(messagingTemplate).convertAndSendToUser(
                "admin", NotificationService.USER_QUEUE_PREFIX, message);
    }

    @Test
    void shouldThrowNullPointerException_whenSendToUserReceivesNullUsername() {
        // Arrange
        final var message = new NotificationMessage(
                "id-3", "INFO", "Title", "Content", null, Instant.now());

        // Act & Assert
        assertThatThrownBy(() -> notificationService.sendToUser(null, message))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("username must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenSendToUserReceivesNullMessage() {
        // Act & Assert
        assertThatThrownBy(() -> notificationService.sendToUser("admin", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("message must not be null");
    }

    @Test
    void shouldExposeCorrectDestinationConstants() {
        // Assert
        assertThat(NotificationService.NOTIFICATIONS_TOPIC).isEqualTo("/topic/notifications");
        assertThat(NotificationService.USER_QUEUE_PREFIX).isEqualTo("/queue/notifications");
    }
}
