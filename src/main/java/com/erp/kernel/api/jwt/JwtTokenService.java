package com.erp.kernel.api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service for creating and validating JWT tokens.
 *
 * <p>Uses HMAC-SHA256 signing with configurable expiration and issuer claims.
 * Tokens include the username as the subject and roles as a custom claim.
 */
@Service
public class JwtTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenService.class);

    private final SecretKey signingKey;
    private final long expirationMinutes;
    private final String issuer;

    /**
     * Creates a new JWT token service.
     *
     * @param properties the JWT configuration properties
     */
    public JwtTokenService(final JwtProperties properties) {
        Objects.requireNonNull(properties, "properties must not be null");
        Objects.requireNonNull(properties.secretKey(), "JWT secret key must not be null");
        this.signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(properties.secretKey()));
        this.expirationMinutes = properties.expirationMinutes();
        this.issuer = properties.issuer();
        LOG.info("JWT token service initialised with expiration of {} minutes", expirationMinutes);
    }

    /**
     * Generates a JWT token for the given username and roles.
     *
     * @param username the username (subject)
     * @param roles    the user's roles
     * @return the signed JWT token string
     */
    public String generateToken(final String username, final List<String> roles) {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(roles, "roles must not be null");
        final var now = Instant.now();
        final var expiration = now.plus(Duration.ofMinutes(expirationMinutes));
        return Jwts.builder()
                .subject(username)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .claim("roles", roles)
                .signWith(signingKey)
                .compact();
    }

    /**
     * Validates a JWT token and extracts the claims.
     *
     * @param token the JWT token string
     * @return an {@link Optional} containing the claims if valid, or empty if invalid
     */
    public Optional<Claims> validateToken(final String token) {
        Objects.requireNonNull(token, "token must not be null");
        try {
            final var claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(claims);
        } catch (final JwtException ex) {
            LOG.debug("JWT validation failed: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Extracts the username (subject) from a valid token.
     *
     * @param token the JWT token string
     * @return an {@link Optional} containing the username if the token is valid
     */
    public Optional<String> extractUsername(final String token) {
        return validateToken(token).map(Claims::getSubject);
    }

    /**
     * Extracts the roles from a valid token.
     *
     * @param token the JWT token string
     * @return the list of roles, or an empty list if the token is invalid
     */
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(final String token) {
        return validateToken(token)
                .map(claims -> (List<String>) claims.get("roles", List.class))
                .orElse(List.of());
    }
}
