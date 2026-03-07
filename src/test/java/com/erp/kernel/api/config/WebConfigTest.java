package com.erp.kernel.api.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link WebConfig}.
 */
@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    @Mock
    private CorsRegistry corsRegistry;

    @Mock
    private CorsRegistration corsRegistration;

    @Mock
    private ContentNegotiationConfigurer contentNegotiationConfigurer;

    @Test
    void shouldThrowNullPointerException_whenCorsPropertiesIsNull() {
        // Act & Assert
        assertThatNullPointerException()
                .isThrownBy(() -> new WebConfig(null))
                .withMessage("corsProperties must not be null");
    }

    @Test
    void shouldCreateInstance_whenValidCorsProperties() {
        // Arrange
        final var properties = new CorsProperties(
                List.of("*"), List.of("GET"), List.of("*"), false, 3600L);

        // Act
        final var config = new WebConfig(properties);

        // Assert
        assertThat(config).isNotNull();
    }

    @Test
    void shouldConfigureCorsMappings_whenAddCorsMappingsCalled() {
        // Arrange
        final var origins = List.of("http://localhost:4200", "http://localhost:3000");
        final var methods = List.of("GET", "POST", "PUT");
        final var headers = List.of("Authorization", "Content-Type");
        final var properties = new CorsProperties(origins, methods, headers, true, 1800L);
        final var config = new WebConfig(properties);

        when(corsRegistry.addMapping("/api/**")).thenReturn(corsRegistration);
        when(corsRegistration.allowedOriginPatterns(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowCredentials(true)).thenReturn(corsRegistration);
        when(corsRegistration.maxAge(1800L)).thenReturn(corsRegistration);

        // Act
        config.addCorsMappings(corsRegistry);

        // Assert
        verify(corsRegistry).addMapping("/api/**");
        verify(corsRegistration).allowedOriginPatterns("http://localhost:4200", "http://localhost:3000");
        verify(corsRegistration).allowedMethods("GET", "POST", "PUT");
        verify(corsRegistration).allowedHeaders("Authorization", "Content-Type");
        verify(corsRegistration).allowCredentials(true);
        verify(corsRegistration).maxAge(1800L);
    }

    @Test
    void shouldConfigureContentNegotiation_whenConfigureCalled() {
        // Arrange
        final var properties = new CorsProperties(
                List.of("*"), List.of("GET"), List.of("*"), false, 3600L);
        final var config = new WebConfig(properties);

        when(contentNegotiationConfigurer.favorParameter(false)).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.ignoreAcceptHeader(false)).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.defaultContentType(any(MediaType.class)))
                .thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.mediaType(any(String.class), any(MediaType.class)))
                .thenReturn(contentNegotiationConfigurer);

        // Act
        config.configureContentNegotiation(contentNegotiationConfigurer);

        // Assert
        verify(contentNegotiationConfigurer).favorParameter(false);
        verify(contentNegotiationConfigurer).ignoreAcceptHeader(false);
        verify(contentNegotiationConfigurer).defaultContentType(MediaType.APPLICATION_JSON);
        verify(contentNegotiationConfigurer).mediaType("json", MediaType.APPLICATION_JSON);
        verify(contentNegotiationConfigurer).mediaType("xml", MediaType.APPLICATION_XML);
    }
}
