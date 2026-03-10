package com.erp.kernel.api.config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CorsProperties}.
 */
class CorsPropertiesTest {

    @Test
    void shouldReturnAllFields_whenConstructedWithValues() {
        // Arrange
        final var origins = List.of("http://localhost:4200");
        final var methods = List.of("GET", "POST");
        final var headers = List.of("Authorization");
        final var allowCredentials = true;
        final var maxAge = 3600L;

        // Act
        final var properties = new CorsProperties(origins, methods, headers, allowCredentials, maxAge);

        // Assert
        assertThat(properties.allowedOrigins()).isEqualTo(origins);
        assertThat(properties.allowedMethods()).isEqualTo(methods);
        assertThat(properties.allowedHeaders()).isEqualTo(headers);
        assertThat(properties.allowCredentials()).isTrue();
        assertThat(properties.maxAge()).isEqualTo(3600L);
    }

    @Test
    void shouldSupportCredentialsFalse_whenConstructedWithFalse() {
        // Arrange & Act
        final var properties = new CorsProperties(List.of("*"), List.of("GET"), List.of("*"), false, 1800L);

        // Assert
        assertThat(properties.allowCredentials()).isFalse();
        assertThat(properties.maxAge()).isEqualTo(1800L);
    }

    @Test
    void shouldBeEqual_whenSameValues() {
        // Arrange
        final var props1 = new CorsProperties(List.of("*"), List.of("GET"), List.of("*"), false, 3600L);
        final var props2 = new CorsProperties(List.of("*"), List.of("GET"), List.of("*"), false, 3600L);

        // Assert
        assertThat(props1).isEqualTo(props2);
        assertThat(props1.hashCode()).isEqualTo(props2.hashCode());
    }

    @Test
    void shouldNotBeEqual_whenDifferentValues() {
        // Arrange
        final var props1 = new CorsProperties(List.of("*"), List.of("GET"), List.of("*"), false, 3600L);
        final var props2 = new CorsProperties(List.of("http://example.com"), List.of("POST"), List.of("*"), true, 1800L);

        // Assert
        assertThat(props1).isNotEqualTo(props2);
    }

    @Test
    void shouldReturnReadableString_whenToStringCalled() {
        // Arrange
        final var properties = new CorsProperties(List.of("*"), List.of("GET"), List.of("*"), false, 3600L);

        // Act
        final var result = properties.toString();

        // Assert
        assertThat(result).contains("CorsProperties");
    }
}
