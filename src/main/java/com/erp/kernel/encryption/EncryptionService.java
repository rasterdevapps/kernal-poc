package com.erp.kernel.encryption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

/**
 * Service providing AES-256-GCM encryption and decryption for sensitive data.
 *
 * <p>Each encryption operation generates a unique random initialisation vector (IV),
 * ensuring that encrypting the same plaintext multiple times produces different ciphertexts.
 * The output format is: {@code Base64(IV || ciphertext || GCM-tag)}.
 */
@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    private final SecretKeySpec secretKey;
    private final SecureRandom secureRandom;

    /**
     * Creates a new encryption service with the configured secret key.
     *
     * @param properties the encryption configuration properties
     */
    @Autowired
    public EncryptionService(final EncryptionProperties properties) {
        Objects.requireNonNull(properties, "properties must not be null");
        Objects.requireNonNull(properties.getSecretKey(), "secretKey must not be null");
        final var decodedKey = Base64.getDecoder().decode(properties.getSecretKey());
        this.secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
        this.secureRandom = new SecureRandom();
    }

    /**
     * Package-private constructor for testing with custom key and random source.
     *
     * @param secretKey    the AES secret key specification
     * @param secureRandom the secure random generator
     */
    EncryptionService(final SecretKeySpec secretKey, final SecureRandom secureRandom) {
        this.secretKey = Objects.requireNonNull(secretKey, "secretKey must not be null");
        this.secureRandom = Objects.requireNonNull(secureRandom, "secureRandom must not be null");
    }

    /**
     * Encrypts the given plaintext using AES-256-GCM.
     *
     * @param plaintext the text to encrypt
     * @return the Base64-encoded ciphertext (IV prepended)
     * @throws EncryptionException if encryption fails
     */
    public String encrypt(final String plaintext) {
        Objects.requireNonNull(plaintext, "plaintext must not be null");
        try {
            final var iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);

            final var cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            final var ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            final var combined = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (final GeneralSecurityException e) {
            throw new EncryptionException("Failed to encrypt data", e);
        }
    }

    /**
     * Decrypts the given Base64-encoded ciphertext using AES-256-GCM.
     *
     * @param encryptedText the Base64-encoded ciphertext to decrypt
     * @return the decrypted plaintext
     * @throws EncryptionException if decryption fails
     */
    public String decrypt(final String encryptedText) {
        Objects.requireNonNull(encryptedText, "encryptedText must not be null");
        try {
            final var combined = Base64.getDecoder().decode(encryptedText);

            final var iv = new byte[GCM_IV_LENGTH];
            final var ciphertext = new byte[combined.length - GCM_IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(combined, GCM_IV_LENGTH, ciphertext, 0, ciphertext.length);

            final var cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            final var decrypted = cipher.doFinal(ciphertext);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (final GeneralSecurityException e) {
            throw new EncryptionException("Failed to decrypt data", e);
        }
    }
}
