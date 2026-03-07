package com.erp.kernel.ddic.controller;

import com.erp.kernel.ddic.dto.CreateExtensionFieldValueRequest;
import com.erp.kernel.ddic.dto.ExtensionFieldValueDto;
import com.erp.kernel.ddic.exception.ValidationException;
import com.erp.kernel.ddic.service.ExtensionFieldService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the {@link ExtensionFieldController}.
 */
@WebMvcTest(ExtensionFieldController.class)
class ExtensionFieldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ExtensionFieldService extensionFieldService;

    @Test
    void shouldSaveExtensionFieldValue_whenValidRequest() throws Exception {
        final var request = new CreateExtensionFieldValueRequest("CUSTOMERS", 1L, "Z_CUSTOM", "value");
        final var response = new ExtensionFieldValueDto(1L, "CUSTOMERS", 1L, "Z_CUSTOM", "value", Instant.now(), Instant.now());
        when(extensionFieldService.save(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/ddic/extensions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fieldName").value("Z_CUSTOM"));
    }

    @Test
    void shouldReturnBadRequest_whenFieldNameInvalid() throws Exception {
        final var request = new CreateExtensionFieldValueRequest("CUSTOMERS", 1L, "INVALID", "value");
        when(extensionFieldService.save(any())).thenThrow(new ValidationException("Must start with Z_"));

        mockMvc.perform(post("/api/v1/ddic/extensions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnExtensionFieldValues_whenRecordFound() throws Exception {
        when(extensionFieldService.findByRecord("CUSTOMERS", 1L)).thenReturn(List.of(
                new ExtensionFieldValueDto(1L, "CUSTOMERS", 1L, "Z_CUSTOM", "value", Instant.now(), Instant.now())));

        mockMvc.perform(get("/api/v1/ddic/extensions")
                        .param("tableName", "CUSTOMERS")
                        .param("recordId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldDeleteExtensionFieldValue_whenIdExists() throws Exception {
        mockMvc.perform(delete("/api/v1/ddic/extensions/1"))
                .andExpect(status().isNoContent());

        verify(extensionFieldService).delete(1L);
    }
}
