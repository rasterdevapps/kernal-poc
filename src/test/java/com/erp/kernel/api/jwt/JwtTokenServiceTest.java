package com.erp.kernel.api.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@link JwtTokenService}.
 */
class JwtTokenServiceTest {

    /** A valid 256-bit key encoded as Base64. */
    private static final String SECRET_KEY =
            "dGhpcy1pcy1hLXZhbGlkLTMyLWJ5dGUta2V5LTEyMzQ=";

    private static final String ISSUER = "test-issuer";
    private static final long EXPIRATION_MINUTES = 60;

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        final var properties = new JwtProperties(SECRET_KEY, EXPIRATION_MINUTES, ISSUER);
        jwtTokenService = new JwtTokenService(properties);
    }

    @Test
    void shouldGenerateToken_whenValidInputs() {
        final var token = jwtTokenService.generateToken("admin", List.of("ADMIN", "USER"));

        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void shouldValidateToken_whenTokenIsValid() {
        final var token = jwtTokenService.generateToken("admin", List.of("ADMIN"));

        final var claims = jwtTokenService.validateToken(token);

        assertThat(claims).isPresent();
        assertThat(claims.get().getSubject()).isEqualTo("admin");
        assertThat(claims.get().getIssuer()).isEqualTo(ISSUER);
    }

    @Test
    void shouldReturnEmpty_whenTokenIsInvalid() {
        final var claims = jwtTokenService.validateToken("invalid.token.here");

        assertThat(claims).isEmpty();
    }

    @Test
    void shouldReturnEmpty_whenTokenHasWrongSignature() {
        final var otherKey = "YW5vdGhlci12YWxpZC0zMi1ieXRlLWtleS1mb3Itanc=";
        final var otherProperties = new JwtProperties(otherKey, EXPIRATION_MINUTES, ISSUER);
        final var otherService = new JwtTokenService(otherProperties);

        final var token = otherService.generateToken("admin", List.of());

        final var claims = jwtTokenService.validateToken(token);
        assertThat(claims).isEmpty();
    }

    @Test
    void shouldReturnEmpty_whenTokenHasWrongIssuer() {
        final var otherProperties = new JwtProperties(SECRET_KEY, EXPIRATION_MINUTES, "wrong-issuer");
        final var otherService = new JwtTokenService(otherProperties);
        final var token = otherService.generateToken("admin", List.of());

        final var claims = jwtTokenService.validateToken(token);
        assertThat(claims).isEmpty();
    }

    @Test
    void shouldExtractUsername_whenTokenIsValid() {
        final var token = jwtTokenService.generateToken("testuser", List.of());

        final var username = jwtTokenService.extractUsername(token);

        assertThat(username).isPresent().contains("testuser");
    }

    @Test
    void shouldReturnEmpty_whenExtractUsernameFromInvalidToken() {
        final var username = jwtTokenService.extractUsername("bad.token.value");

        assertThat(username).isEmpty();
    }

    @Test
    void shouldExtractRoles_whenTokenIsValid() {
        final var token = jwtTokenService.generateToken("admin", List.of("ADMIN", "USER"));

        final var roles = jwtTokenService.extractRoles(token);

        assertThat(roles).containsExactly("ADMIN", "USER");
    }

    @Test
    void shouldReturnEmptyRoles_whenTokenIsInvalid() {
        final var roles = jwtTokenService.extractRoles("bad.token.value");

        assertThat(roles).isEmpty();
    }

    @Test
    void shouldReturnEmptyRoles_whenTokenHasNoRoles() {
        final var token = jwtTokenService.generateToken("user", List.of());

        final var roles = jwtTokenService.extractRoles(token);

        assertThat(roles).isEmpty();
    }

    @Test
    void shouldThrowException_whenPropertiesIsNull() {
        assertThatThrownBy(() -> new JwtTokenService(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("properties must not be null");
    }

    @Test
    void shouldThrowException_whenSecretKeyIsNull() {
        final var properties = new JwtProperties(null, 60, "issuer");

        assertThatThrownBy(() -> new JwtTokenService(properties))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("JWT secret key must not be null");
    }

    @Test
    void shouldThrowException_whenUsernameIsNull() {
        assertThatThrownBy(() -> jwtTokenService.generateToken(null, List.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("username must not be null");
    }

    @Test
    void shouldThrowException_whenRolesIsNull() {
        assertThatThrownBy(() -> jwtTokenService.generateToken("user", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("roles must not be null");
    }

    @Test
    void shouldThrowException_whenValidateTokenWithNullToken() {
        assertThatThrownBy(() -> jwtTokenService.validateToken(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("token must not be null");
    }

    @Test
    void shouldIncludeExpirationInClaims() {
        final var token = jwtTokenService.generateToken("user", List.of());

        final var claims = jwtTokenService.validateToken(token);

        assertThat(claims).isPresent();
        assertThat(claims.get().getExpiration()).isNotNull();
        assertThat(claims.get().getIssuedAt()).isNotNull();
    }

    @Test
    void shouldReturnEmpty_whenTokenIsExpired() {
        // Use negative expiration to guarantee the token is expired at validation time
        final var expiredProperties = new JwtProperties(SECRET_KEY, -1, ISSUER);
        final var expiredService = new JwtTokenService(expiredProperties);

        final var token = expiredService.generateToken("user", List.of());

        final var claims = expiredService.validateToken(token);
        assertThat(claims).isEmpty();
    }
}
