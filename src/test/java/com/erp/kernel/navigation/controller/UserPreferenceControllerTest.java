package com.erp.kernel.navigation.controller;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateUserPreferenceRequest;
import com.erp.kernel.navigation.dto.UserPreferenceDto;
import com.erp.kernel.navigation.service.UserPreferenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the {@link UserPreferenceController}.
 */
@WebMvcTest(UserPreferenceController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserPreferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserPreferenceService userPreferenceService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldCreatePreference_whenValidRequest() throws Exception {
        final var request = new CreateUserPreferenceRequest(1L, 2L, "en", "yyyy-MM-dd", "HH:mm:ss", 20);
        final var now = Instant.now();
        final var response = new UserPreferenceDto(1L, 1L, 2L, "en", "yyyy-MM-dd", "HH:mm:ss", 20, now, now);
        when(userPreferenceService.create(any(CreateUserPreferenceRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/navigation/preferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void shouldReturnPreference_whenFoundById() throws Exception {
        final var now = Instant.now();
        final var response = new UserPreferenceDto(1L, 1L, 2L, "en", "yyyy-MM-dd", "HH:mm:ss", 20, now, now);
        when(userPreferenceService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/navigation/preferences/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnPreferenceForUser() throws Exception {
        final var now = Instant.now();
        final var response = new UserPreferenceDto(1L, 1L, 2L, "en", "yyyy-MM-dd", "HH:mm:ss", 20, now, now);
        when(userPreferenceService.findByUserId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/navigation/preferences/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void shouldUpdatePreference() throws Exception {
        final var request = new CreateUserPreferenceRequest(1L, 3L, "de", "dd.MM.yyyy", "HH:mm", 50);
        final var now = Instant.now();
        final var response = new UserPreferenceDto(1L, 1L, 3L, "de", "dd.MM.yyyy", "HH:mm", 50, now, now);
        when(userPreferenceService.update(eq(1L), any(CreateUserPreferenceRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/navigation/preferences/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locale").value("de"));
    }

    @Test
    void shouldDeletePreference() throws Exception {
        mockMvc.perform(delete("/api/v1/navigation/preferences/1"))
                .andExpect(status().isNoContent());

        verify(userPreferenceService).delete(1L);
    }

    @Test
    void shouldReturnNotFound_whenPreferenceNotFound() throws Exception {
        when(userPreferenceService.findById(99L)).thenThrow(new EntityNotFoundException("Preference not found"));

        mockMvc.perform(get("/api/v1/navigation/preferences/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnConflict_whenPreferenceExistsForUser() throws Exception {
        final var request = new CreateUserPreferenceRequest(1L, 2L, "en", "yyyy-MM-dd", "HH:mm:ss", 20);
        when(userPreferenceService.create(any())).thenThrow(new DuplicateEntityException("Preference already exists for user"));

        mockMvc.perform(post("/api/v1/navigation/preferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
