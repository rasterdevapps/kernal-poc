package com.erp.kernel.ddic.controller;

import com.erp.kernel.ddic.dto.CreateDataElementRequest;
import com.erp.kernel.ddic.dto.DataElementDto;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.service.DataElementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Tests for the {@link DataElementController}.
 */
@WebMvcTest(DataElementController.class)
class DataElementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DataElementService dataElementService;

    @Test
    void shouldCreateDataElement_whenValidRequest() throws Exception {
        final var request = new CreateDataElementRequest("CUST_NAME", 1L, "CN", "Cust Name", "Customer Name", "Desc");
        final var response = new DataElementDto(1L, "CUST_NAME", 1L, "CHAR_30", "CN", "Cust Name", "Customer Name", "Desc", Instant.now(), Instant.now());
        when(dataElementService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/ddic/data-elements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.elementName").value("CUST_NAME"));
    }

    @Test
    void shouldReturnBadRequest_whenElementNameIsBlank() throws Exception {
        final var request = new CreateDataElementRequest("", 1L, null, null, null, null);

        mockMvc.perform(post("/api/v1/ddic/data-elements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenDomainIdIsNull() throws Exception {
        final var json = "{\"elementName\":\"ELEM\",\"domainId\":null}";

        mockMvc.perform(post("/api/v1/ddic/data-elements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnDataElement_whenFoundById() throws Exception {
        final var response = new DataElementDto(1L, "ELEM", 1L, "DOM", "E", "Elem", "Element", "Desc", Instant.now(), Instant.now());
        when(dataElementService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/ddic/data-elements/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnNotFound_whenDataElementNotFound() throws Exception {
        when(dataElementService.findById(99L)).thenThrow(new EntityNotFoundException("Not found"));

        mockMvc.perform(get("/api/v1/ddic/data-elements/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllDataElements() throws Exception {
        when(dataElementService.findAll()).thenReturn(List.of(
                new DataElementDto(1L, "E1", 1L, "D1", "L1", "M1", "Long1", "Desc1", Instant.now(), Instant.now())));

        mockMvc.perform(get("/api/v1/ddic/data-elements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldUpdateDataElement_whenValidRequest() throws Exception {
        final var request = new CreateDataElementRequest("NEW_NAME", 2L, "N", "New", "New Name", "Updated");
        final var response = new DataElementDto(1L, "NEW_NAME", 2L, "DOM", "N", "New", "New Name", "Updated", Instant.now(), Instant.now());
        when(dataElementService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/ddic/data-elements/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elementName").value("NEW_NAME"));
    }

    @Test
    void shouldDeleteDataElement_whenIdExists() throws Exception {
        mockMvc.perform(delete("/api/v1/ddic/data-elements/1"))
                .andExpect(status().isNoContent());

        verify(dataElementService).delete(1L);
    }
}
