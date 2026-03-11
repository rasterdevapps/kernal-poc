package com.erp.kernel.plugin;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link PluginController}.
 */
@WebMvcTest(PluginController.class)
@AutoConfigureMockMvc(addFilters = false)
class PluginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PluginRegistry pluginRegistry;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldReturnAllPlugins() throws Exception {
        final var plugin = createPlugin("core", "Core Module", "1.0.0");
        when(pluginRegistry.getAll()).thenReturn(List.of(plugin));

        mockMvc.perform(get("/api/v1/plugins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("core"))
                .andExpect(jsonPath("$[0].name").value("Core Module"))
                .andExpect(jsonPath("$[0].version").value("1.0.0"));
    }

    @Test
    void shouldReturnEmptyList_whenNoPluginsRegistered() throws Exception {
        when(pluginRegistry.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/plugins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldReturnPlugin_whenFound() throws Exception {
        final var plugin = createPlugin("health", "Health Module", "2.0.0");
        when(pluginRegistry.findById("health")).thenReturn(Optional.of(plugin));

        mockMvc.perform(get("/api/v1/plugins/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("health"))
                .andExpect(jsonPath("$.name").value("Health Module"))
                .andExpect(jsonPath("$.version").value("2.0.0"));
    }

    @Test
    void shouldReturn404_whenPluginNotFound() throws Exception {
        when(pluginRegistry.findById("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/plugins/nonexistent"))
                .andExpect(status().isNotFound());
    }

    private static Plugin createPlugin(final String id, final String name,
                                       final String version) {
        return new Plugin() {
            @Override
            public String getId() {
                return id;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getVersion() {
                return version;
            }

            @Override
            public PluginState getState() {
                return PluginState.STARTED;
            }

            @Override
            public List<String> getDependencies() {
                return List.of();
            }

            @Override
            public void initialize(final PluginContext context) {
                // no-op for testing
            }

            @Override
            public void start() {
                // no-op for testing
            }

            @Override
            public void stop() {
                // no-op for testing
            }

            @Override
            public void destroy() {
                // no-op for testing
            }
        };
    }
}
