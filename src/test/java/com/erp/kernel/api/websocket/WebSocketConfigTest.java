package com.erp.kernel.api.websocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link WebSocketConfig}.
 */
@ExtendWith(MockitoExtension.class)
class WebSocketConfigTest {

    @Mock
    private MessageBrokerRegistry brokerRegistry;

    @Mock
    private StompEndpointRegistry endpointRegistry;

    @Mock
    private StompWebSocketEndpointRegistration endpointRegistration;

    @Mock
    private org.springframework.web.socket.config.annotation.SockJsServiceRegistration sockJsRegistration;

    private WebSocketConfig webSocketConfig;

    @BeforeEach
    void setUp() {
        webSocketConfig = new WebSocketConfig();
    }

    @Test
    void shouldEnableSimpleBroker_whenConfiguringMessageBroker() {
        // Arrange — nothing extra needed

        // Act
        webSocketConfig.configureMessageBroker(brokerRegistry);

        // Assert
        verify(brokerRegistry).enableSimpleBroker(
                WebSocketConfig.BROKER_TOPIC, WebSocketConfig.BROKER_QUEUE);
        verify(brokerRegistry).setApplicationDestinationPrefixes(WebSocketConfig.APP_PREFIX);
    }

    @Test
    void shouldRegisterStompEndpoint_whenRegisteringEndpoints() {
        // Arrange
        when(endpointRegistry.addEndpoint(WebSocketConfig.WS_ENDPOINT))
                .thenReturn(endpointRegistration);
        when(endpointRegistration.setAllowedOriginPatterns("*"))
                .thenReturn(endpointRegistration);
        when(endpointRegistration.withSockJS()).thenReturn(sockJsRegistration);

        // Act
        webSocketConfig.registerStompEndpoints(endpointRegistry);

        // Assert
        verify(endpointRegistry).addEndpoint(WebSocketConfig.WS_ENDPOINT);
        verify(endpointRegistration).setAllowedOriginPatterns("*");
        verify(endpointRegistration).withSockJS();
    }

    @Test
    void shouldExposeCorrectConstants() {
        // Assert
        assertThat(WebSocketConfig.WS_ENDPOINT).isEqualTo("/ws");
        assertThat(WebSocketConfig.BROKER_TOPIC).isEqualTo("/topic");
        assertThat(WebSocketConfig.BROKER_QUEUE).isEqualTo("/queue");
        assertThat(WebSocketConfig.APP_PREFIX).isEqualTo("/app");
    }
}
