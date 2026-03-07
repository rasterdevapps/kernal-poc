package com.erp.kernel.api.jwt;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link JwtProperties} record.
 */
class JwtPropertiesTest {

    @Test
    void shouldStoreAllProperties() {
        final var properties = new JwtProperties("c2VjcmV0", 30, "test-issuer");

        assertThat(properties.secretKey()).isEqualTo("c2VjcmV0");
        assertThat(properties.expirationMinutes()).isEqualTo(30);
        assertThat(properties.issuer()).isEqualTo("test-issuer");
    }

    @Test
    void shouldSupportNullValues() {
        final var properties = new JwtProperties(null, 0, null);

        assertThat(properties.secretKey()).isNull();
        assertThat(properties.expirationMinutes()).isZero();
        assertThat(properties.issuer()).isNull();
    }

    @Test
    void shouldImplementEquals() {
        final var props1 = new JwtProperties("key", 60, "issuer");
        final var props2 = new JwtProperties("key", 60, "issuer");
        final var props3 = new JwtProperties("other", 60, "issuer");

        assertThat(props1).isEqualTo(props2);
        assertThat(props1).isNotEqualTo(props3);
    }

    @Test
    void shouldImplementHashCode() {
        final var props1 = new JwtProperties("key", 60, "issuer");
        final var props2 = new JwtProperties("key", 60, "issuer");

        assertThat(props1.hashCode()).isEqualTo(props2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        final var properties = new JwtProperties("key", 60, "issuer");

        assertThat(properties.toString()).contains("key", "60", "issuer");
    }
}
