package com.erp.kernel.navigation.controller;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateRecentNavigationRequest;
import com.erp.kernel.navigation.dto.RecentNavigationDto;
import com.erp.kernel.navigation.service.RecentNavigationService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the {@link RecentNavigationController}.
 */
@WebMvcTest(RecentNavigationController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecentNavigationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecentNavigationService recentNavigationService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldRecordNavigation_whenValidRequest() throws Exception {
        final var request = new CreateRecentNavigationRequest(1L, 2L);
        final var now = Instant.now();
        final var response = new RecentNavigationDto(1L, 1L, 2L, now, now, now);
        when(recentNavigationService.record(any(CreateRecentNavigationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/navigation/recent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void shouldReturnNavigation_whenFoundById() throws Exception {
        final var now = Instant.now();
        final var response = new RecentNavigationDto(1L, 1L, 2L, now, now, now);
        when(recentNavigationService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/navigation/recent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnNavigationsForUser() throws Exception {
        final var now = Instant.now();
        when(recentNavigationService.findByUserId(1L)).thenReturn(List.of(
                new RecentNavigationDto(1L, 1L, 2L, now, now, now)));

        mockMvc.perform(get("/api/v1/navigation/recent/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldDeleteNavigationsForUser() throws Exception {
        mockMvc.perform(delete("/api/v1/navigation/recent/user/1"))
                .andExpect(status().isNoContent());

        verify(recentNavigationService).deleteByUserId(1L);
    }

    @Test
    void shouldReturnNotFound_whenNavigationNotFound() throws Exception {
        when(recentNavigationService.findById(99L)).thenThrow(new EntityNotFoundException("Navigation not found"));

        mockMvc.perform(get("/api/v1/navigation/recent/99"))
                .andExpect(status().isNotFound());
    }
}
