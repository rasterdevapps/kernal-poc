package com.erp.kernel.ddic.controller;

import com.erp.kernel.ddic.dto.CreateTableDefinitionRequest;
import com.erp.kernel.ddic.dto.TableDefinitionDto;
import com.erp.kernel.ddic.model.SchemaLevel;
import com.erp.kernel.ddic.service.TableDefinitionService;
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
 * Tests for the {@link TableDefinitionController}.
 */
@WebMvcTest(TableDefinitionController.class)
class TableDefinitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TableDefinitionService tableDefinitionService;

    @Test
    void shouldCreateTableDefinition_whenValidRequest() throws Exception {
        final var request = new CreateTableDefinitionRequest("CUSTOMERS", SchemaLevel.CONCEPTUAL, "Customer table", false);
        final var response = new TableDefinitionDto(1L, "CUSTOMERS", SchemaLevel.CONCEPTUAL, "Customer table", false, Instant.now(), Instant.now());
        when(tableDefinitionService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/ddic/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tableName").value("CUSTOMERS"));
    }

    @Test
    void shouldReturnBadRequest_whenTableNameIsBlank() throws Exception {
        final var request = new CreateTableDefinitionRequest("", SchemaLevel.EXTERNAL, null, false);

        mockMvc.perform(post("/api/v1/ddic/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnTableDefinition_whenFoundById() throws Exception {
        final var response = new TableDefinitionDto(1L, "ORDERS", SchemaLevel.INTERNAL, "desc", false, Instant.now(), Instant.now());
        when(tableDefinitionService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/ddic/tables/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tableName").value("ORDERS"));
    }

    @Test
    void shouldReturnAllTableDefinitions_whenNoFilter() throws Exception {
        when(tableDefinitionService.findAll()).thenReturn(List.of(
                new TableDefinitionDto(1L, "T1", SchemaLevel.CONCEPTUAL, "d1", false, Instant.now(), Instant.now())));

        mockMvc.perform(get("/api/v1/ddic/tables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldReturnFilteredTableDefinitions_whenSchemaLevelProvided() throws Exception {
        when(tableDefinitionService.findBySchemaLevel(SchemaLevel.EXTERNAL)).thenReturn(List.of(
                new TableDefinitionDto(1L, "V_CUST", SchemaLevel.EXTERNAL, "d", false, Instant.now(), Instant.now())));

        mockMvc.perform(get("/api/v1/ddic/tables").param("schemaLevel", "EXTERNAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].schemaLevel").value("EXTERNAL"));
    }

    @Test
    void shouldUpdateTableDefinition_whenValidRequest() throws Exception {
        final var request = new CreateTableDefinitionRequest("NEW_TABLE", SchemaLevel.INTERNAL, "Updated", true);
        final var response = new TableDefinitionDto(1L, "NEW_TABLE", SchemaLevel.INTERNAL, "Updated", true, Instant.now(), Instant.now());
        when(tableDefinitionService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/ddic/tables/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tableName").value("NEW_TABLE"));
    }

    @Test
    void shouldDeleteTableDefinition_whenIdExists() throws Exception {
        mockMvc.perform(delete("/api/v1/ddic/tables/1"))
                .andExpect(status().isNoContent());

        verify(tableDefinitionService).delete(1L);
    }
}
