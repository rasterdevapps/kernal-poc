package com.erp.kernel.api.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * Servlet filter that validates JWT tokens on incoming requests.
 *
 * <p>Extracts the token from the {@code Authorization: Bearer <token>} header,
 * validates it using {@link JwtTokenService}, and sets request attributes
 * for downstream use. Public endpoints (auth, actuator, Swagger) are excluded.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    /** Paths that do not require JWT authentication. */
    static final Set<String> PUBLIC_PATHS = Set.of(
            "/api/v1/auth/token",
            "/actuator",
            "/swagger-ui",
            "/v3/api-docs",
            "/ws"
    );

    /** Request attribute key for the authenticated username. */
    public static final String ATTR_USERNAME = "erp.auth.username";

    /** Request attribute key for the authenticated user's roles. */
    public static final String ATTR_ROLES = "erp.auth.roles";

    private final JwtTokenService jwtTokenService;

    /**
     * Creates a new JWT authentication filter.
     *
     * @param jwtTokenService the JWT token service
     */
    public JwtAuthenticationFilter(final JwtTokenService jwtTokenService) {
        this.jwtTokenService = Objects.requireNonNull(
                jwtTokenService, "jwtTokenService must not be null");
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {

        if (isPublicPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"message\":\"Missing or invalid Authorization header\"}");
            response.setContentType("application/json");
            return;
        }

        final var token = authHeader.substring(BEARER_PREFIX.length());
        final var claimsOpt = jwtTokenService.validateToken(token);
        if (claimsOpt.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"message\":\"Invalid or expired token\"}");
            response.setContentType("application/json");
            return;
        }

        final var username = claimsOpt.get().getSubject();
        final var roles = jwtTokenService.extractRoles(token);
        request.setAttribute(ATTR_USERNAME, username);
        request.setAttribute(ATTR_ROLES, roles);

        LOG.debug("Authenticated request from user '{}'", username);
        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(final String uri) {
        return PUBLIC_PATHS.stream().anyMatch(uri::startsWith);
    }
}
