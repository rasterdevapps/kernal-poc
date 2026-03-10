package com.erp.kernel.api.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configures WebSocket support with STOMP messaging protocol.
 *
 * <p>Provides real-time data push capabilities for notifications,
 * live dashboards, and collaborative editing scenarios.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /** WebSocket endpoint path. */
    static final String WS_ENDPOINT = "/ws";

    /** Message broker destination prefix for subscriptions. */
    static final String BROKER_TOPIC = "/topic";

    /** Message broker destination prefix for user-specific messages. */
    static final String BROKER_QUEUE = "/queue";

    /** Application destination prefix for messages from clients. */
    static final String APP_PREFIX = "/app";

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry config) {
        config.enableSimpleBroker(BROKER_TOPIC, BROKER_QUEUE);
        config.setApplicationDestinationPrefixes(APP_PREFIX);
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint(WS_ENDPOINT)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
