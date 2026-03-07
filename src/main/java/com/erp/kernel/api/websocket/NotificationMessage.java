package com.erp.kernel.api.websocket;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a notification message sent via WebSocket.
 *
 * @param id        the unique notification identifier
 * @param type      the notification type (e.g., INFO, WARNING, ERROR)
 * @param title     the notification title
 * @param content   the notification content
 * @param recipient the target username (null for broadcast)
 * @param timestamp the creation timestamp
 */
public record NotificationMessage(
        String id,
        String type,
        String title,
        String content,
        String recipient,
        Instant timestamp
) {

    /**
     * Creates a broadcast notification.
     *
     * @param type    the notification type
     * @param title   the title
     * @param content the content
     * @return a broadcast notification with generated ID and current timestamp
     */
    public static NotificationMessage broadcast(final String type,
                                                 final String title,
                                                 final String content) {
        return new NotificationMessage(
                UUID.randomUUID().toString(),
                type, title, content, null, Instant.now());
    }

    /**
     * Creates a user-targeted notification.
     *
     * @param type      the notification type
     * @param title     the title
     * @param content   the content
     * @param recipient the target username
     * @return a targeted notification with generated ID and current timestamp
     */
    public static NotificationMessage targeted(final String type,
                                                final String title,
                                                final String content,
                                                final String recipient) {
        return new NotificationMessage(
                UUID.randomUUID().toString(),
                type, title, content, recipient, Instant.now());
    }
}
