package com.erp.kernel.navigation.controller;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateTCodeRequest;
import com.erp.kernel.navigation.dto.TCodeDto;
import com.erp.kernel.navigation.service.TCodeService;
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
 * Tests for the {@link TCodeController}.
 */
@WebMvcTest(TCodeController.class)
@AutoConfigureMockMvc(addFilters = false)
class TCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TCodeService tCodeService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldCreateTCode_whenValidRequest() throws Exception {
        final var request = new CreateTCodeRequest("SU01", "User Maintenance", "SECURITY", "/security/users", "person");
        final var now = Instant.now();
        final var response = new TCodeDto(1L, "SU01", "User Maintenance", "SECURITY", "/security/users", "person", true, now, now);
        when(tCodeService.create(any(CreateTCodeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/navigation/tcodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("SU01"));
    }

    @Test
    void shouldReturnTCode_whenFoundById() throws Exception {
        final var now = Instant.now();
        final var response = new TCodeDto(1L, "SU01", "User Maintenance", "SECURITY", "/security/users", "person", true, now, now);
        when(tCodeService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/navigation/tcodes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnAllTCodes() throws Exception {
        final var now = Instant.now();
        when(tCodeService.findAll()).thenReturn(List.of(
                new TCodeDto(1L, "SU01", "User Maintenance", "SECURITY", "/security/users", "person", true, now, now)));

        mockMvc.perform(get("/api/v1/navigation/tcodes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldReturnTCode_whenFoundByCode() throws Exception {
        final var now = Instant.now();
        final var response = new TCodeDto(1L, "SU01", "User Maintenance", "SECURITY", "/security/users", "person", true, now, now);
        when(tCodeService.findByCode("SU01")).thenReturn(response);

        mockMvc.perform(get("/api/v1/navigation/tcodes/code/SU01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SU01"));
    }

    @Test
    void shouldReturnTCodesByModule() throws Exception {
        final var now = Instant.now();
        when(tCodeService.findByModule("ADMIN")).thenReturn(List.of(
                new TCodeDto(1L, "SU01", "User Maintenance", "ADMIN", "/admin/users", "person", true, now, now)));

        mockMvc.perform(get("/api/v1/navigation/tcodes/module/ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldReturnActiveTCodes() throws Exception {
        final var now = Instant.now();
        when(tCodeService.findAllActive()).thenReturn(List.of(
                new TCodeDto(1L, "SU01", "User Maintenance", "SECURITY", "/security/users", "person", true, now, now)));

        mockMvc.perform(get("/api/v1/navigation/tcodes/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldUpdateTCode() throws Exception {
        final var request = new CreateTCodeRequest("SU02", "Updated", "SECURITY", "/security/updated", "edit");
        final var now = Instant.now();
        final var response = new TCodeDto(1L, "SU02", "Updated", "SECURITY", "/security/updated", "edit", true, now, now);
        when(tCodeService.update(eq(1L), any(CreateTCodeRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/navigation/tcodes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SU02"));
    }

    @Test
    void shouldDeleteTCode() throws Exception {
        mockMvc.perform(delete("/api/v1/navigation/tcodes/1"))
                .andExpect(status().isNoContent());

        verify(tCodeService).delete(1L);
    }

    @Test
    void shouldReturnNotFound_whenTCodeNotFound() throws Exception {
        when(tCodeService.findById(99L)).thenThrow(new EntityNotFoundException("TCode not found"));

        mockMvc.perform(get("/api/v1/navigation/tcodes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnConflict_whenTCodeExists() throws Exception {
        final var request = new CreateTCodeRequest("SU01", "User Maintenance", "SECURITY", "/security/users", "person");
        when(tCodeService.create(any())).thenThrow(new DuplicateEntityException("TCode already exists"));

        mockMvc.perform(post("/api/v1/navigation/tcodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
