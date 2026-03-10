package com.erp.kernel.navigation.controller;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateThemeRequest;
import com.erp.kernel.navigation.dto.ThemeDto;
import com.erp.kernel.navigation.service.ThemeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
 * Tests for the {@link ThemeController}.
 */
@WebMvcTest(ThemeController.class)
@AutoConfigureMockMvc(addFilters = false)
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ThemeService themeService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldCreateTheme_whenValidRequest() throws Exception {
        final var request = new CreateThemeRequest("Dark Theme", "A dark theme", "#000000", "#FFFFFF", false);
        final var now = Instant.now();
        final var response = new ThemeDto(1L, "Dark Theme", "A dark theme", "#000000", "#FFFFFF", true, false, now, now);
        when(themeService.create(any(CreateThemeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/navigation/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.themeName").value("Dark Theme"));
    }

    @Test
    void shouldReturnTheme_whenFoundById() throws Exception {
        final var now = Instant.now();
        final var response = new ThemeDto(1L, "Dark Theme", "A dark theme", "#000000", "#FFFFFF", true, false, now, now);
        when(themeService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/navigation/themes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnAllThemes() throws Exception {
        final var now = Instant.now();
        when(themeService.findAll()).thenReturn(List.of(
                new ThemeDto(1L, "Dark Theme", "A dark theme", "#000000", "#FFFFFF", true, false, now, now)));

        mockMvc.perform(get("/api/v1/navigation/themes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldUpdateTheme() throws Exception {
        final var request = new CreateThemeRequest("Updated Theme", "Updated", "#111111", "#EEEEEE", true);
        final var now = Instant.now();
        final var response = new ThemeDto(1L, "Updated Theme", "Updated", "#111111", "#EEEEEE", true, true, now, now);
        when(themeService.update(eq(1L), any(CreateThemeRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/navigation/themes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.themeName").value("Updated Theme"));
    }

    @Test
    void shouldDeleteTheme() throws Exception {
        mockMvc.perform(delete("/api/v1/navigation/themes/1"))
                .andExpect(status().isNoContent());

        verify(themeService).delete(1L);
    }

    @Test
    void shouldReturnNotFound_whenThemeNotFound() throws Exception {
        when(themeService.findById(99L)).thenThrow(new EntityNotFoundException("Theme not found"));

        mockMvc.perform(get("/api/v1/navigation/themes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnConflict_whenThemeNameExists() throws Exception {
        final var request = new CreateThemeRequest("Dark Theme", "A dark theme", "#000000", "#FFFFFF", false);
        when(themeService.create(any())).thenThrow(new DuplicateEntityException("Theme already exists"));

        mockMvc.perform(post("/api/v1/navigation/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
