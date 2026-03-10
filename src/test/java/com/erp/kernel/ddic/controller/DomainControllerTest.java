package com.erp.kernel.ddic.controller;

import com.erp.kernel.ddic.dto.CreateDomainRequest;
import com.erp.kernel.ddic.dto.DomainDto;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.service.DomainService;
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
import com.erp.kernel.api.config.CorsProperties;

/**
 * Tests for the {@link DomainController}.
 */
@WebMvcTest(DomainController.class)
@AutoConfigureMockMvc(addFilters = false)
class DomainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DomainService domainService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldCreateDomain_whenValidRequest() throws Exception {
        final var request = new CreateDomainRequest("CHAR_30", "CHAR", 30, 0, "Character domain");
        final var response = new DomainDto(1L, "CHAR_30", "CHAR", 30, 0, "Character domain", Instant.now(), Instant.now());
        when(domainService.create(any(CreateDomainRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/ddic/domains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.domainName").value("CHAR_30"));
    }

    @Test
    void shouldReturnConflict_whenDomainNameExists() throws Exception {
        final var request = new CreateDomainRequest("CHAR_30", "CHAR", 30, 0, "desc");
        when(domainService.create(any())).thenThrow(new DuplicateEntityException("Already exists"));

        mockMvc.perform(post("/api/v1/ddic/domains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnBadRequest_whenDomainNameIsBlank() throws Exception {
        final var request = new CreateDomainRequest("", "CHAR", 30, 0, "desc");

        mockMvc.perform(post("/api/v1/ddic/domains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnDomain_whenFoundById() throws Exception {
        final var response = new DomainDto(1L, "CHAR_30", "CHAR", 30, 0, "desc", Instant.now(), Instant.now());
        when(domainService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/ddic/domains/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnNotFound_whenDomainNotFoundById() throws Exception {
        when(domainService.findById(99L)).thenThrow(new EntityNotFoundException("Not found"));

        mockMvc.perform(get("/api/v1/ddic/domains/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllDomains() throws Exception {
        when(domainService.findAll()).thenReturn(List.of(
                new DomainDto(1L, "CHAR_30", "CHAR", 30, 0, "d1", Instant.now(), Instant.now()),
                new DomainDto(2L, "INT_10", "INT", 10, 0, "d2", Instant.now(), Instant.now())));

        mockMvc.perform(get("/api/v1/ddic/domains"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdateDomain_whenValidRequest() throws Exception {
        final var request = new CreateDomainRequest("NEW_NAME", "INT", 10, 0, "Updated");
        final var response = new DomainDto(1L, "NEW_NAME", "INT", 10, 0, "Updated", Instant.now(), Instant.now());
        when(domainService.update(eq(1L), any(CreateDomainRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/ddic/domains/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.domainName").value("NEW_NAME"));
    }

    @Test
    void shouldDeleteDomain_whenIdExists() throws Exception {
        mockMvc.perform(delete("/api/v1/ddic/domains/1"))
                .andExpect(status().isNoContent());

        verify(domainService).delete(1L);
    }

    @Test
    void shouldReturnNotFound_whenDeletingNonExistentDomain() throws Exception {
        doThrow(new EntityNotFoundException("Not found")).when(domainService).delete(99L);

        mockMvc.perform(delete("/api/v1/ddic/domains/99"))
                .andExpect(status().isNotFound());
    }
}
