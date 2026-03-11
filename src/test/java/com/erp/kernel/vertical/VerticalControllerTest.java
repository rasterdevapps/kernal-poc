package com.erp.kernel.vertical;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link VerticalController}.
 */
@WebMvcTest(VerticalController.class)
@AutoConfigureMockMvc(addFilters = false)
class VerticalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VerticalRegistry verticalRegistry;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldReturnAllModules_whenGetAllCalled() throws Exception {
        final var module = createHealthcareModule();
        when(verticalRegistry.getAll()).thenReturn(List.of(module));

        mockMvc.perform(get("/api/v1/verticals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("health-01"));
    }

    @Test
    void shouldReturnEmptyArray_whenNoModulesRegistered() throws Exception {
        when(verticalRegistry.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/verticals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldReturnModule_whenFindByIdFindsModule() throws Exception {
        final var module = createHealthcareModule();
        when(verticalRegistry.findById("health-01")).thenReturn(Optional.of(module));

        mockMvc.perform(get("/api/v1/verticals/health-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("health-01"))
                .andExpect(jsonPath("$.name").value("Patient Module"))
                .andExpect(jsonPath("$.verticalType").value("HEALTHCARE"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }

    @Test
    void shouldReturnNotFound_whenFindByIdDoesNotFindModule() throws Exception {
        when(verticalRegistry.findById("non-existent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/verticals/non-existent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnModulesByType_whenFindByTypeCalled() throws Exception {
        final var module = createHealthcareModule();
        when(verticalRegistry.findByVerticalType(VerticalType.HEALTHCARE))
                .thenReturn(List.of(module));

        mockMvc.perform(get("/api/v1/verticals/by-type")
                        .param("type", "HEALTHCARE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("health-01"));
    }

    @Test
    void shouldReturnEmptyArray_whenFindByTypeFindsNoModules() throws Exception {
        when(verticalRegistry.findByVerticalType(VerticalType.FINANCE))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/verticals/by-type")
                        .param("type", "FINANCE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    private VerticalDescriptor createHealthcareModule() {
        return new VerticalDescriptor(
                "health-01", "Patient Module", VerticalType.HEALTHCARE,
                "1.0.0", "Patient management", List.of("persistence", "audit"));
    }
}
