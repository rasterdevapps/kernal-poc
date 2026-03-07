package com.erp.kernel.encryption;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link EncryptionProperties}.
 */
class EncryptionPropertiesTest {

    @Test
    void shouldGetAndSetSecretKey() {
        final var properties = new EncryptionProperties();
        properties.setSecretKey("dGVzdC1zZWNyZXQta2V5LTMyLWJ5dGVzLWxvbmch");

        assertThat(properties.getSecretKey()).isEqualTo("dGVzdC1zZWNyZXQta2V5LTMyLWJ5dGVzLWxvbmch");
    }

    @Test
    void shouldReturnNullSecretKeyByDefault() {
        final var properties = new EncryptionProperties();

        assertThat(properties.getSecretKey()).isNull();
    }

    @Test
    void shouldThrowNullPointerException_whenSecretKeyIsNull() {
        final var properties = new EncryptionProperties();

        assertThatThrownBy(() -> properties.setSecretKey(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("secretKey must not be null");
    }
}
