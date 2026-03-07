package com.erp.kernel.api.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link JwtAuthenticationFilter}.
 */
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Claims validClaims;

    private JwtAuthenticationFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtTokenService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/api/v1/auth/token",
            "/actuator/health",
            "/actuator",
            "/swagger-ui/index.html",
            "/swagger-ui",
            "/v3/api-docs",
            "/v3/api-docs/swagger-config",
            "/ws/connect"
    })
    void shouldAllowPublicPaths_withoutToken(final String path) throws ServletException, IOException {
        request.setRequestURI(path);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void shouldReturnUnauthorized_whenNoAuthorizationHeader() throws ServletException, IOException {
        request.setRequestURI("/api/v1/some-resource");

        filter.doFilterInternal(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString())
                .contains("Missing or invalid Authorization header");
        verifyNoInteractions(filterChain);
    }

    @Test
    void shouldReturnUnauthorized_whenAuthorizationHeaderHasNoBearer()
            throws ServletException, IOException {
        request.setRequestURI("/api/v1/some-resource");
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");

        filter.doFilterInternal(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString())
                .contains("Missing or invalid Authorization header");
        verifyNoInteractions(filterChain);
    }

    @Test
    void shouldReturnUnauthorized_whenTokenIsInvalid() throws ServletException, IOException {
        request.setRequestURI("/api/v1/some-resource");
        request.addHeader("Authorization", "Bearer invalid-token");
        when(jwtTokenService.validateToken("invalid-token")).thenReturn(Optional.empty());

        filter.doFilterInternal(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString()).contains("Invalid or expired token");
        verifyNoInteractions(filterChain);
    }

    @Test
    void shouldAuthenticateRequest_whenTokenIsValid() throws ServletException, IOException {
        request.setRequestURI("/api/v1/some-resource");
        request.addHeader("Authorization", "Bearer valid-token");

        when(validClaims.getSubject()).thenReturn("admin");
        when(jwtTokenService.validateToken("valid-token"))
                .thenReturn(Optional.of(validClaims));
        when(jwtTokenService.extractRoles("valid-token"))
                .thenReturn(List.of("ADMIN"));

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(request.getAttribute(JwtAuthenticationFilter.ATTR_USERNAME))
                .isEqualTo("admin");
        assertThat(request.getAttribute(JwtAuthenticationFilter.ATTR_ROLES))
                .isEqualTo(List.of("ADMIN"));
    }

    @Test
    void shouldSetEmptyRoles_whenTokenHasNoRoles() throws ServletException, IOException {
        request.setRequestURI("/api/v1/data");
        request.addHeader("Authorization", "Bearer valid-token");

        when(validClaims.getSubject()).thenReturn("user");
        when(jwtTokenService.validateToken("valid-token"))
                .thenReturn(Optional.of(validClaims));
        when(jwtTokenService.extractRoles("valid-token"))
                .thenReturn(List.of());

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(request.getAttribute(JwtAuthenticationFilter.ATTR_ROLES))
                .isEqualTo(List.of());
    }

    @Test
    void shouldThrowException_whenJwtTokenServiceIsNull() {
        assertThatThrownBy(() -> new JwtAuthenticationFilter(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("jwtTokenService must not be null");
    }

    @Test
    void shouldReturnJsonContentType_whenUnauthorizedDueToMissingHeader()
            throws ServletException, IOException {
        request.setRequestURI("/api/v1/secure");

        filter.doFilterInternal(request, response, filterChain);

        assertThat(response.getContentType()).isEqualTo("application/json");
    }

    @Test
    void shouldReturnJsonContentType_whenUnauthorizedDueToInvalidToken()
            throws ServletException, IOException {
        request.setRequestURI("/api/v1/secure");
        request.addHeader("Authorization", "Bearer bad");
        when(jwtTokenService.validateToken("bad")).thenReturn(Optional.empty());

        filter.doFilterInternal(request, response, filterChain);

        assertThat(response.getContentType()).isEqualTo("application/json");
    }

    @Test
    void shouldContainExpectedPublicPaths() {
        assertThat(JwtAuthenticationFilter.PUBLIC_PATHS).containsExactlyInAnyOrder(
                "/api/v1/auth/token",
                "/actuator",
                "/swagger-ui",
                "/v3/api-docs",
                "/ws"
        );
    }
}
