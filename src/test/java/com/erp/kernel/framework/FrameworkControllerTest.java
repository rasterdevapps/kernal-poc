package com.erp.kernel.framework;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the {@link FrameworkController}.
 */
@WebMvcTest(FrameworkController.class)
@AutoConfigureMockMvc(addFilters = false)
class FrameworkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private FrameworkInfoService frameworkInfoService;

    @MockitoBean
    private ErpModuleRegistry moduleRegistry;

    @Test
    void shouldReturnFrameworkInfo_whenInfoEndpointCalled() throws Exception {
        // Arrange
        var info = new FrameworkInfo("ERP Kernel", "1.0.0",
                List.of("Database Abstraction", "Security"));
        when(frameworkInfoService.getFrameworkInfo()).thenReturn(info);

        // Act & Assert
        mockMvc.perform(get("/api/v1/framework/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ERP Kernel"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.capabilities").isArray())
                .andExpect(jsonPath("$.capabilities[0]").value("Database Abstraction"))
                .andExpect(jsonPath("$.capabilities[1]").value("Security"));
    }

    @Test
    void shouldReturnModules_whenModulesEndpointCalled() throws Exception {
        // Arrange
        var module = ErpModuleDescriptor.of("core", "1.0.0", "Core module");
        when(moduleRegistry.getAll()).thenReturn(List.of(module));

        // Act & Assert
        mockMvc.perform(get("/api/v1/framework/modules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("core"))
                .andExpect(jsonPath("$[0].version").value("1.0.0"))
                .andExpect(jsonPath("$[0].description").value("Core module"));
    }

    @Test
    void shouldReturnEmptyArray_whenNoModulesRegistered() throws Exception {
        // Arrange
        when(moduleRegistry.getAll()).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/v1/framework/modules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
