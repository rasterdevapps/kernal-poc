package com.erp.kernel.ddic.controller;

import com.erp.kernel.ddic.dto.CreateSearchHelpRequest;
import com.erp.kernel.ddic.dto.SearchHelpDto;
import com.erp.kernel.ddic.service.SearchHelpService;
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
 * Tests for the {@link SearchHelpController}.
 */
@WebMvcTest(SearchHelpController.class)
@AutoConfigureMockMvc(addFilters = false)
class SearchHelpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SearchHelpService searchHelpService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldCreateSearchHelp_whenValidRequest() throws Exception {
        final var request = new CreateSearchHelpRequest("SH_CUST", 1L, "Customer search");
        final var response = new SearchHelpDto(1L, "SH_CUST", 1L, "CUSTOMERS", "Customer search", Instant.now(), Instant.now());
        when(searchHelpService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/ddic/search-helps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.searchHelpName").value("SH_CUST"));
    }

    @Test
    void shouldReturnBadRequest_whenSearchHelpNameIsBlank() throws Exception {
        final var request = new CreateSearchHelpRequest("", 1L, null);

        mockMvc.perform(post("/api/v1/ddic/search-helps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnSearchHelp_whenFoundById() throws Exception {
        final var response = new SearchHelpDto(1L, "SH_ORDER", 2L, "ORDERS", "desc", Instant.now(), Instant.now());
        when(searchHelpService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/ddic/search-helps/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchHelpName").value("SH_ORDER"));
    }

    @Test
    void shouldReturnAllSearchHelps() throws Exception {
        when(searchHelpService.findAll()).thenReturn(List.of(
                new SearchHelpDto(1L, "SH1", 1L, "T1", "d1", Instant.now(), Instant.now())));

        mockMvc.perform(get("/api/v1/ddic/search-helps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldUpdateSearchHelp_whenValidRequest() throws Exception {
        final var request = new CreateSearchHelpRequest("NEW_SH", 2L, "Updated");
        final var response = new SearchHelpDto(1L, "NEW_SH", 2L, "ORDERS", "Updated", Instant.now(), Instant.now());
        when(searchHelpService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/ddic/search-helps/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchHelpName").value("NEW_SH"));
    }

    @Test
    void shouldDeleteSearchHelp_whenIdExists() throws Exception {
        mockMvc.perform(delete("/api/v1/ddic/search-helps/1"))
                .andExpect(status().isNoContent());

        verify(searchHelpService).delete(1L);
    }
}
