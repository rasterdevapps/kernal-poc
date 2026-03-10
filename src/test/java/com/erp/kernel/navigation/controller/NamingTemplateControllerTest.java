package com.erp.kernel.navigation.controller;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateNamingTemplateRequest;
import com.erp.kernel.navigation.dto.NamingTemplateDto;
import com.erp.kernel.navigation.service.NamingTemplateService;
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
 * Tests for the {@link NamingTemplateController}.
 */
@WebMvcTest(NamingTemplateController.class)
@AutoConfigureMockMvc(addFilters = false)
class NamingTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NamingTemplateService namingTemplateService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldCreateNamingTemplate_whenValidRequest() throws Exception {
        final var request = new CreateNamingTemplateRequest("SCREEN", "{MODULE}_{TYPE}_{NAME}", "Screen naming", "ADMIN_LIST_USERS");
        final var now = Instant.now();
        final var response = new NamingTemplateDto(1L, "SCREEN", "{MODULE}_{TYPE}_{NAME}", "Screen naming", "ADMIN_LIST_USERS", now, now);
        when(namingTemplateService.create(any(CreateNamingTemplateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/navigation/naming-templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.entityType").value("SCREEN"));
    }

    @Test
    void shouldReturnNamingTemplate_whenFoundById() throws Exception {
        final var now = Instant.now();
        final var response = new NamingTemplateDto(1L, "SCREEN", "{MODULE}_{TYPE}_{NAME}", "Screen naming", "ADMIN_LIST_USERS", now, now);
        when(namingTemplateService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/navigation/naming-templates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnAllNamingTemplates() throws Exception {
        final var now = Instant.now();
        when(namingTemplateService.findAll()).thenReturn(List.of(
                new NamingTemplateDto(1L, "SCREEN", "{MODULE}_{TYPE}_{NAME}", "Screen naming", "ADMIN_LIST_USERS", now, now)));

        mockMvc.perform(get("/api/v1/navigation/naming-templates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldUpdateNamingTemplate() throws Exception {
        final var request = new CreateNamingTemplateRequest("COMPONENT", "{MODULE}_{NAME}", "Updated", "ADMIN_HEADER");
        final var now = Instant.now();
        final var response = new NamingTemplateDto(1L, "COMPONENT", "{MODULE}_{NAME}", "Updated", "ADMIN_HEADER", now, now);
        when(namingTemplateService.update(eq(1L), any(CreateNamingTemplateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/navigation/naming-templates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entityType").value("COMPONENT"));
    }

    @Test
    void shouldDeleteNamingTemplate() throws Exception {
        mockMvc.perform(delete("/api/v1/navigation/naming-templates/1"))
                .andExpect(status().isNoContent());

        verify(namingTemplateService).delete(1L);
    }

    @Test
    void shouldReturnNotFound_whenNamingTemplateNotFound() throws Exception {
        when(namingTemplateService.findById(99L)).thenThrow(new EntityNotFoundException("NamingTemplate not found"));

        mockMvc.perform(get("/api/v1/navigation/naming-templates/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnConflict_whenEntityTypeExists() throws Exception {
        final var request = new CreateNamingTemplateRequest("SCREEN", "{MODULE}_{TYPE}_{NAME}", "Screen naming", "ADMIN_LIST_USERS");
        when(namingTemplateService.create(any())).thenThrow(new DuplicateEntityException("Entity type already exists"));

        mockMvc.perform(post("/api/v1/navigation/naming-templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
