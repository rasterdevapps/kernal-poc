package com.erp.kernel.api.controller;

import com.erp.kernel.api.dto.TokenRequest;
import com.erp.kernel.api.jwt.JwtProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import com.erp.kernel.security.auth.AuthenticationProviderType;
import com.erp.kernel.security.auth.AuthenticationResult;
import com.erp.kernel.security.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.erp.kernel.api.config.CorsProperties;

/**
 * Tests for the {@link AuthTokenController}.
 */
@WebMvcTest(AuthTokenController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthTokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private JwtProperties jwtProperties;

    @Test
    void shouldIssueToken_whenAuthenticationSucceeds() throws Exception {
        final var request = new TokenRequest("admin", "password");
        final var authResult = AuthenticationResult.success("admin",
                AuthenticationProviderType.LOCAL);
        when(authenticationService.authenticate("admin", "password"))
                .thenReturn(authResult);
        when(jwtTokenService.generateToken(eq("admin"), anyList()))
                .thenReturn("generated-jwt-token");
        when(jwtProperties.expirationMinutes()).thenReturn(60L);

        mockMvc.perform(post("/api/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("generated-jwt-token"))
                .andExpect(jsonPath("$.expiresIn").value(3600))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void shouldReturnUnauthorized_whenAuthenticationFails() throws Exception {
        final var request = new TokenRequest("admin", "wrong");
        final var authResult = AuthenticationResult.failure("admin",
                AuthenticationProviderType.LOCAL, "Bad credentials");
        when(authenticationService.authenticate("admin", "wrong"))
                .thenReturn(authResult);

        mockMvc.perform(post("/api/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(
                        "Authentication failed for user 'admin'"));
    }

    @Test
    void shouldReturnUnauthorized_whenMfaRequired() throws Exception {
        final var request = new TokenRequest("admin", "password");
        final var authResult = AuthenticationResult.mfaRequired("admin",
                AuthenticationProviderType.LOCAL);
        when(authenticationService.authenticate("admin", "password"))
                .thenReturn(authResult);

        mockMvc.perform(post("/api/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(
                        "MFA verification required for user 'admin'"));
    }

    @Test
    void shouldReturnBadRequest_whenUsernameIsBlank() throws Exception {
        final var request = new TokenRequest("", "password");

        mockMvc.perform(post("/api/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenPasswordIsBlank() throws Exception {
        final var request = new TokenRequest("admin", "");

        mockMvc.perform(post("/api/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenBodyIsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
