package com.erp.kernel.ddic.controller;

import com.erp.kernel.ddic.dto.CreateTableFieldRequest;
import com.erp.kernel.ddic.dto.TableFieldDto;
import com.erp.kernel.ddic.exception.ValidationException;
import com.erp.kernel.ddic.service.TableFieldService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.erp.kernel.api.config.CorsProperties;

/**
 * Tests for the {@link TableFieldController}.
 */
@WebMvcTest(TableFieldController.class)
@AutoConfigureMockMvc(addFilters = false)
class TableFieldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TableFieldService tableFieldService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldCreateTableField_whenValidRequest() throws Exception {
        final var request = new CreateTableFieldRequest(1L, "CUSTOMER_ID", 2L, 1, true, false, false);
        final var response = new TableFieldDto(1L, 1L, "CUSTOMER_ID", 2L, "CUST_ID_ELEM", 1, true, false, false, Instant.now(), Instant.now());
        when(tableFieldService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/ddic/fields")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fieldName").value("CUSTOMER_ID"));
    }

    @Test
    void shouldReturnBadRequest_whenExtensionFieldNameInvalid() throws Exception {
        final var request = new CreateTableFieldRequest(1L, "INVALID", 2L, 1, false, true, true);
        when(tableFieldService.create(any())).thenThrow(new ValidationException("Must start with Z_"));

        mockMvc.perform(post("/api/v1/ddic/fields")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnTableField_whenFoundById() throws Exception {
        final var response = new TableFieldDto(1L, 1L, "FIELD", 2L, "ELEM", 1, false, true, false, Instant.now(), Instant.now());
        when(tableFieldService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/ddic/fields/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fieldName").value("FIELD"));
    }

    @Test
    void shouldReturnFieldsByTableDefinition() throws Exception {
        when(tableFieldService.findByTableDefinitionId(1L)).thenReturn(List.of(
                new TableFieldDto(1L, 1L, "F1", 2L, "E1", 1, true, false, false, Instant.now(), Instant.now())));

        mockMvc.perform(get("/api/v1/ddic/fields").param("tableDefinitionId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldReturnExtensionFieldsOnly_whenFlagIsTrue() throws Exception {
        when(tableFieldService.findExtensionFields(1L)).thenReturn(List.of(
                new TableFieldDto(1L, 1L, "Z_CUSTOM", 2L, "ELEM", 10, false, true, true, Instant.now(), Instant.now())));

        mockMvc.perform(get("/api/v1/ddic/fields")
                        .param("tableDefinitionId", "1")
                        .param("extensionsOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].extension").value(true));
    }

    @Test
    void shouldDeleteTableField_whenIdExists() throws Exception {
        mockMvc.perform(delete("/api/v1/ddic/fields/1"))
                .andExpect(status().isNoContent());

        verify(tableFieldService).delete(1L);
    }
}
