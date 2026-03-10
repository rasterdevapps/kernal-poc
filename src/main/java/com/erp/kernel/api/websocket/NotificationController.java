package com.erp.kernel.api.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Objects;

/**
 * WebSocket controller for handling notification messages.
 *
 * <p>Receives messages from clients via STOMP and broadcasts
 * them to subscribed clients on the notifications topic.
 */
@Controller
public class NotificationController {

    /**
     * Handles incoming notification messages and broadcasts them.
     *
     * @param message the incoming notification message
     * @return the notification message to broadcast
     */
    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public NotificationMessage handleNotification(final NotificationMessage message) {
        Objects.requireNonNull(message, "message must not be null");
        return message;
    }
}
