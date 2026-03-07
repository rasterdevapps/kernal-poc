package com.erp.kernel.security.controller;

import com.erp.kernel.security.dto.CreateLoginPolicyRequest;
import com.erp.kernel.security.dto.LoginPolicyDto;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.security.service.LoginPolicyService;
import com.erp.kernel.api.jwt.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the {@link LoginPolicyController}.
 */
@WebMvcTest(LoginPolicyController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginPolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoginPolicyService loginPolicyService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldCreatePolicy_whenValidRequest() throws Exception {
        final var request = new CreateLoginPolicyRequest("DEFAULT", "Default policy",
                8, true, true, true, true, 90, 5, 30, 480, false);
        final var now = Instant.now();
        final var response = new LoginPolicyDto(1L, "DEFAULT", "Default policy",
                8, true, true, true, true, 90, 5, 30, 480, false, true, now, now);
        when(loginPolicyService.create(any(CreateLoginPolicyRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/security/login-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.policyName").value("DEFAULT"));
    }

    @Test
    void shouldReturnPolicy_whenFoundById() throws Exception {
        final var now = Instant.now();
        final var response = new LoginPolicyDto(1L, "DEFAULT", "desc",
                8, true, true, true, true, 90, 5, 30, 480, false, true, now, now);
        when(loginPolicyService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/security/login-policies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnAllPolicies() throws Exception {
        final var now = Instant.now();
        when(loginPolicyService.findAll()).thenReturn(List.of(
                new LoginPolicyDto(1L, "DEFAULT", "d", 8, true, true, true, true,
                        90, 5, 30, 480, false, true, now, now)));

        mockMvc.perform(get("/api/v1/security/login-policies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldUpdatePolicy() throws Exception {
        final var request = new CreateLoginPolicyRequest("UPDATED", "Updated",
                10, true, true, true, true, 60, 3, 60, 240, true);
        final var now = Instant.now();
        final var response = new LoginPolicyDto(1L, "UPDATED", "Updated",
                10, true, true, true, true, 60, 3, 60, 240, true, true, now, now);
        when(loginPolicyService.update(eq(1L), any(CreateLoginPolicyRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/security/login-policies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyName").value("UPDATED"));
    }

    @Test
    void shouldDeletePolicy() throws Exception {
        mockMvc.perform(delete("/api/v1/security/login-policies/1"))
                .andExpect(status().isNoContent());

        verify(loginPolicyService).delete(1L);
    }

    @Test
    void shouldReturnConflict_whenPolicyNameExists() throws Exception {
        final var request = new CreateLoginPolicyRequest("DEFAULT", "desc",
                8, true, true, true, true, 90, 5, 30, 480, false);
        when(loginPolicyService.create(any())).thenThrow(new DuplicateEntityException("exists"));

        mockMvc.perform(post("/api/v1/security/login-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFound_whenPolicyNotFound() throws Exception {
        when(loginPolicyService.findById(99L)).thenThrow(new EntityNotFoundException("Not found"));

        mockMvc.perform(get("/api/v1/security/login-policies/99"))
                .andExpect(status().isNotFound());
    }
}
