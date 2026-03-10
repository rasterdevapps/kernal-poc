package com.erp.kernel.navigation.controller;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateScreenRequest;
import com.erp.kernel.navigation.dto.ScreenDto;
import com.erp.kernel.navigation.service.ScreenService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the {@link ScreenController}.
 */
@WebMvcTest(ScreenController.class)
@AutoConfigureMockMvc(addFilters = false)
class ScreenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ScreenService screenService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldCreateScreen_whenValidRequest() throws Exception {
        final var request = new CreateScreenRequest("SCR_ADMIN_USERS", "User Management", "Admin user list", "ADMIN", 1L, "LIST");
        final var now = Instant.now();
        final var response = new ScreenDto(1L, "SCR_ADMIN_USERS", "User Management", "Admin user list", "ADMIN", 1L, "LIST", now, now);
        when(screenService.create(any(CreateScreenRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/navigation/screens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.screenId").value("SCR_ADMIN_USERS"));
    }

    @Test
    void shouldReturnScreen_whenFoundById() throws Exception {
        final var now = Instant.now();
        final var response = new ScreenDto(1L, "SCR_ADMIN_USERS", "User Management", "Admin user list", "ADMIN", 1L, "LIST", now, now);
        when(screenService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/navigation/screens/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnAllScreens() throws Exception {
        final var now = Instant.now();
        when(screenService.findAll()).thenReturn(List.of(
                new ScreenDto(1L, "SCR_ADMIN_USERS", "User Management", "Admin user list", "ADMIN", 1L, "LIST", now, now)));

        mockMvc.perform(get("/api/v1/navigation/screens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldReturnScreensByModule() throws Exception {
        final var now = Instant.now();
        when(screenService.findByModule("ADMIN")).thenReturn(List.of(
                new ScreenDto(1L, "SCR_ADMIN_USERS", "User Management", "Admin user list", "ADMIN", 1L, "LIST", now, now)));

        mockMvc.perform(get("/api/v1/navigation/screens/module/ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldUpdateScreen() throws Exception {
        final var request = new CreateScreenRequest("SCR_ADMIN_ROLES", "Role Management", "Updated", "ADMIN", 2L, "DETAIL");
        final var now = Instant.now();
        final var response = new ScreenDto(1L, "SCR_ADMIN_ROLES", "Role Management", "Updated", "ADMIN", 2L, "DETAIL", now, now);
        when(screenService.update(eq(1L), any(CreateScreenRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/navigation/screens/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.screenId").value("SCR_ADMIN_ROLES"));
    }

    @Test
    void shouldDeleteScreen() throws Exception {
        mockMvc.perform(delete("/api/v1/navigation/screens/1"))
                .andExpect(status().isNoContent());

        verify(screenService).delete(1L);
    }

    @Test
    void shouldReturnNotFound_whenScreenNotFound() throws Exception {
        when(screenService.findById(99L)).thenThrow(new EntityNotFoundException("Screen not found"));

        mockMvc.perform(get("/api/v1/navigation/screens/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnConflict_whenScreenIdExists() throws Exception {
        final var request = new CreateScreenRequest("SCR_ADMIN_USERS", "User Management", "Admin user list", "ADMIN", 1L, "LIST");
        when(screenService.create(any())).thenThrow(new DuplicateEntityException("Screen already exists"));

        mockMvc.perform(post("/api/v1/navigation/screens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
