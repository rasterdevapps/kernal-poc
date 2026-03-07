package com.erp.kernel.security.auth;

import com.erp.kernel.security.entity.User;
import com.erp.kernel.security.exception.AuthenticationException;
import com.erp.kernel.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

/**
 * Authentication provider for local database credentials.
 *
 * <p>Authenticates users against password hashes stored in the database.
 * Uses SHA-256 with salt for password hashing.
 */
@Component
public class LocalAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(LocalAuthenticationProvider.class);
    private static final int SALT_LENGTH = 16;

    private final UserRepository userRepository;

    /**
     * Creates a new local authentication provider.
     *
     * @param userRepository the user repository
     */
    public LocalAuthenticationProvider(final UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
    }

    @Override
    public AuthenticationProviderType getType() {
        return AuthenticationProviderType.LOCAL;
    }

    @Override
    public AuthenticationResult authenticate(final String username, final String credential) {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(credential, "credential must not be null");
        final var userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            LOG.debug("Authentication failed: user '{}' not found", username);
            return AuthenticationResult.failure(username, AuthenticationProviderType.LOCAL,
                    "Invalid credentials");
        }
        final var user = userOptional.get();
        if (!verifyPassword(credential, user.getPasswordHash())) {
            LOG.debug("Authentication failed: invalid password for user '{}'", username);
            return AuthenticationResult.failure(username, AuthenticationProviderType.LOCAL,
                    "Invalid credentials");
        }
        LOG.info("User '{}' authenticated via local provider", username);
        return AuthenticationResult.success(username, AuthenticationProviderType.LOCAL);
    }

    @Override
    public boolean supports(final AuthenticationProviderType type) {
        return AuthenticationProviderType.LOCAL == type;
    }

    /**
     * Hashes a password with a random salt.
     *
     * @param password the plain text password
     * @return the Base64-encoded salt and hash
     */
    public static String hashPassword(final String password) {
        Objects.requireNonNull(password, "password must not be null");
        final var random = new SecureRandom();
        final var salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        final var hash = computeHash(password, salt);
        final var saltBase64 = Base64.getEncoder().encodeToString(salt);
        final var hashBase64 = Base64.getEncoder().encodeToString(hash);
        return saltBase64 + ":" + hashBase64;
    }

    /**
     * Verifies a password against a stored hash.
     *
     * @param password   the plain text password
     * @param storedHash the stored hash in format salt:hash
     * @return {@code true} if the password matches
     */
    static boolean verifyPassword(final String password, final String storedHash) {
        if (storedHash == null || !storedHash.contains(":")) {
            return false;
        }
        final var parts = storedHash.split(":", 2);
        final var salt = Base64.getDecoder().decode(parts[0]);
        final var expectedHash = Base64.getDecoder().decode(parts[1]);
        final var actualHash = computeHash(password, salt);
        return MessageDigest.isEqual(expectedHash, actualHash);
    }

    private static byte[] computeHash(final String password, final byte[] salt) {
        try {
            final var digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            return digest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new AuthenticationException("Hash algorithm not available", e);
        }
    }
}
