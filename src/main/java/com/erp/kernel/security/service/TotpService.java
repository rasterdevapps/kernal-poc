package com.erp.kernel.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

/**
 * Service for TOTP (Time-based One-Time Password) generation and validation.
 *
 * <p>Implements RFC 6238 TOTP algorithm for two-factor authentication
 * using authenticator apps (Google Authenticator, Microsoft Authenticator, etc.).
 */
@Service
public class TotpService {

    private static final Logger LOG = LoggerFactory.getLogger(TotpService.class);
    private static final int SECRET_LENGTH = 20;
    private static final int CODE_DIGITS = 6;
    private static final int TIME_STEP_SECONDS = 30;
    private static final int ALLOWED_TIME_DRIFT = 1;
    private static final String HMAC_ALGORITHM = "HmacSHA1";

    /**
     * Generates a new TOTP secret key.
     *
     * @return the Base32-encoded secret key
     */
    public String generateSecret() {
        final var random = new SecureRandom();
        final var bytes = new byte[SECRET_LENGTH];
        random.nextBytes(bytes);
        return Base64.getEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Generates a provisioning URI for QR code generation.
     *
     * @param username  the username
     * @param issuer    the issuer name (e.g., application name)
     * @param secretKey the Base32-encoded secret key
     * @return the otpauth:// URI
     */
    public String generateProvisioningUri(final String username, final String issuer,
                                          final String secretKey) {
        return "otpauth://totp/%s:%s?secret=%s&issuer=%s&digits=%d&period=%d"
                .formatted(issuer, username, secretKey, issuer, CODE_DIGITS, TIME_STEP_SECONDS);
    }

    /**
     * Validates a TOTP code against a secret key.
     *
     * @param code      the TOTP code to validate
     * @param secretKey the Base32-encoded secret key
     * @return {@code true} if the code is valid
     */
    public boolean validateCode(final String code, final String secretKey) {
        if (code == null || secretKey == null) {
            return false;
        }

        try {
            final var codeValue = Integer.parseInt(code);
            final var currentTimeStep = Instant.now().getEpochSecond() / TIME_STEP_SECONDS;

            for (int i = -ALLOWED_TIME_DRIFT; i <= ALLOWED_TIME_DRIFT; i++) {
                final var expectedCode = generateCode(secretKey, currentTimeStep + i);
                if (expectedCode == codeValue) {
                    LOG.debug("TOTP code validated successfully");
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            LOG.debug("Invalid TOTP code format");
        }

        return false;
    }

    /**
     * Generates a TOTP code for a given time step.
     *
     * @param secretKey the Base32-encoded secret key
     * @param timeStep  the time step counter
     * @return the TOTP code
     */
    int generateCode(final String secretKey, final long timeStep) {
        final var key = Base64.getDecoder().decode(secretKey);
        final var timeBytes = ByteBuffer.allocate(8).putLong(timeStep).array();

        try {
            final var mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(key, HMAC_ALGORITHM));
            final var hash = mac.doFinal(timeBytes);

            final var offset = hash[hash.length - 1] & 0x0F;
            final var truncatedHash = ((hash[offset] & 0x7F) << 24)
                    | ((hash[offset + 1] & 0xFF) << 16)
                    | ((hash[offset + 2] & 0xFF) << 8)
                    | (hash[offset + 3] & 0xFF);

            return truncatedHash % (int) Math.pow(10, CODE_DIGITS);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("TOTP generation failed", e);
        }
    }
}
