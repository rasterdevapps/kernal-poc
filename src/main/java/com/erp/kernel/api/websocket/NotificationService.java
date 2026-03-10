package com.erp.kernel.api.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service for sending real-time notifications via WebSocket.
 *
 * <p>Supports broadcasting notifications to all connected clients
 * and sending targeted notifications to specific users.
 */
@Service
public class NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    /** Topic destination for broadcast notifications. */
    static final String NOTIFICATIONS_TOPIC = "/topic/notifications";

    /** Queue destination prefix for user-specific notifications. */
    static final String USER_QUEUE_PREFIX = "/queue/notifications";

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Creates a new notification service.
     *
     * @param messagingTemplate the STOMP messaging template
     */
    public NotificationService(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = Objects.requireNonNull(
                messagingTemplate, "messagingTemplate must not be null");
    }

    /**
     * Broadcasts a notification to all connected clients.
     *
     * @param message the notification message
     */
    public void broadcast(final NotificationMessage message) {
        Objects.requireNonNull(message, "message must not be null");
        messagingTemplate.convertAndSend(NOTIFICATIONS_TOPIC, message);
        LOG.info("Broadcast notification: type={}, title={}", message.type(), message.title());
    }

    /**
     * Sends a notification to a specific user.
     *
     * @param username the target username
     * @param message  the notification message
     */
    public void sendToUser(final String username, final NotificationMessage message) {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(message, "message must not be null");
        messagingTemplate.convertAndSendToUser(username, USER_QUEUE_PREFIX, message);
        LOG.info("Sent notification to user '{}': type={}, title={}",
                username, message.type(), message.title());
    }
}
