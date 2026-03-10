package com.erp.kernel.api.controller;

import com.erp.kernel.api.dto.TokenRequest;
import com.erp.kernel.api.dto.TokenResponse;
import com.erp.kernel.api.jwt.JwtProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import com.erp.kernel.security.exception.AuthenticationException;
import com.erp.kernel.security.service.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * REST controller for issuing JWT tokens via OAuth 2.0-style token endpoint.
 *
 * <p>Authenticates users using the existing {@link AuthenticationService} and
 * issues signed JWT tokens upon successful authentication.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthTokenController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthTokenController.class);

    private final AuthenticationService authenticationService;
    private final JwtTokenService jwtTokenService;
    private final JwtProperties jwtProperties;

    /**
     * Creates a new auth token controller.
     *
     * @param authenticationService the authentication service
     * @param jwtTokenService       the JWT token service
     * @param jwtProperties         the JWT configuration properties
     */
    public AuthTokenController(final AuthenticationService authenticationService,
                               final JwtTokenService jwtTokenService,
                               final JwtProperties jwtProperties) {
        this.authenticationService = Objects.requireNonNull(
                authenticationService, "authenticationService must not be null");
        this.jwtTokenService = Objects.requireNonNull(
                jwtTokenService, "jwtTokenService must not be null");
        this.jwtProperties = Objects.requireNonNull(
                jwtProperties, "jwtProperties must not be null");
    }

    /**
     * Issues a JWT token after authenticating the user.
     *
     * @param request the token request containing credentials
     * @return the token response with JWT and expiration
     * @throws AuthenticationException if authentication fails
     */
    @PostMapping("/token")
    public ResponseEntity<TokenResponse> issueToken(
            @Valid @RequestBody final TokenRequest request) {
        final var result = authenticationService.authenticate(
                request.username(), request.password());

        if (!result.authenticated()) {
            throw new AuthenticationException(
                    "Authentication failed for user '%s'".formatted(request.username()));
        }

        if (result.mfaRequired()) {
            throw new AuthenticationException(
                    "MFA verification required for user '%s'".formatted(request.username()));
        }

        final var token = jwtTokenService.generateToken(result.username(), List.of());
        final var expiresInSeconds = jwtProperties.expirationMinutes() * 60;

        LOG.info("JWT token issued for user '{}'", result.username());
        return ResponseEntity.ok(TokenResponse.bearer(token, expiresInSeconds));
    }
}
