package com.erp.kernel.encryption;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link EncryptionService}.
 */
class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        final var properties = new EncryptionProperties();
        // Valid 32-byte AES-256 key encoded as Base64
        final var key = Base64.getEncoder().encodeToString(new byte[32]);
        properties.setSecretKey(key);
        encryptionService = new EncryptionService(properties);
    }

    @Test
    void shouldEncryptAndDecryptSuccessfully() {
        final var plaintext = "Hello, World!";

        final var encrypted = encryptionService.encrypt(plaintext);
        final var decrypted = encryptionService.decrypt(encrypted);

        assertThat(decrypted).isEqualTo(plaintext);
    }

    @Test
    void shouldProduceDifferentCiphertextForSamePlaintext() {
        final var plaintext = "Same text";

        final var encrypted1 = encryptionService.encrypt(plaintext);
        final var encrypted2 = encryptionService.encrypt(plaintext);

        assertThat(encrypted1).isNotEqualTo(encrypted2);
    }

    @Test
    void shouldEncryptEmptyString() {
        final var encrypted = encryptionService.encrypt("");
        final var decrypted = encryptionService.decrypt(encrypted);

        assertThat(decrypted).isEmpty();
    }

    @Test
    void shouldEncryptLongText() {
        final var plaintext = "A".repeat(10000);

        final var encrypted = encryptionService.encrypt(plaintext);
        final var decrypted = encryptionService.decrypt(encrypted);

        assertThat(decrypted).isEqualTo(plaintext);
    }

    @Test
    void shouldThrowNullPointerException_whenPlaintextIsNull() {
        assertThatThrownBy(() -> encryptionService.encrypt(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("plaintext must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenEncryptedTextIsNull() {
        assertThatThrownBy(() -> encryptionService.decrypt(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("encryptedText must not be null");
    }

    @Test
    void shouldThrowEncryptionException_whenDecryptingInvalidData() {
        final var invalidData = Base64.getEncoder().encodeToString(new byte[50]);

        assertThatThrownBy(() -> encryptionService.decrypt(invalidData))
                .isInstanceOf(EncryptionException.class)
                .hasMessage("Failed to decrypt data");
    }

    @Test
    void shouldThrowNullPointerException_whenPropertiesIsNull() {
        assertThatThrownBy(() -> new EncryptionService(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("properties must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenSecretKeyIsNull() {
        final var properties = new EncryptionProperties();

        assertThatThrownBy(() -> new EncryptionService(properties))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("secretKey must not be null");
    }

    @Test
    void shouldHandleUnicodeText() {
        final var plaintext = "Héllo Wörld 日本語 🎉";

        final var encrypted = encryptionService.encrypt(plaintext);
        final var decrypted = encryptionService.decrypt(encrypted);

        assertThat(decrypted).isEqualTo(plaintext);
    }

    @Test
    void shouldThrowEncryptionException_whenEncryptionFailsDueToInvalidKey() {
        final var invalidKey = new SecretKeySpec(new byte[3], "AES");
        final var brokenService = new EncryptionService(invalidKey, new SecureRandom());

        assertThatThrownBy(() -> brokenService.encrypt("test"))
                .isInstanceOf(EncryptionException.class)
                .hasMessage("Failed to encrypt data");
    }
}
