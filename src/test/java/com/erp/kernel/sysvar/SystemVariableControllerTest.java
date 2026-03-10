package com.erp.kernel.sysvar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.erp.kernel.api.jwt.JwtTokenService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.EnumMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.erp.kernel.api.config.CorsProperties;

/**
 * Tests for {@link SystemVariableController}.
 */
@WebMvcTest(SystemVariableController.class)
@AutoConfigureMockMvc(addFilters = false)
class SystemVariableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SystemVariableProvider systemVariableProvider;

    @MockitoBean
    private CorsProperties corsProperties;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldReturnAllVariables() throws Exception {
        final var allValues = new EnumMap<SystemVariable, String>(SystemVariable.class);
        allValues.put(SystemVariable.SY_DATUM, "20260307");
        allValues.put(SystemVariable.SY_UZEIT, "143022");
        when(systemVariableProvider.getAllValues()).thenReturn(allValues);

        mockMvc.perform(get("/api/v1/system/variables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnSingleVariable_whenFound() throws Exception {
        when(systemVariableProvider.getValue(SystemVariable.SY_DATUM)).thenReturn("20260307");

        mockMvc.perform(get("/api/v1/system/variables/SY_DATUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.variableName").value("SY-DATUM"))
                .andExpect(jsonPath("$.value").value("20260307"))
                .andExpect(jsonPath("$.dataType").value("DATS"));
    }

    @Test
    void shouldReturn404_whenVariableNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/system/variables/INVALID_NAME"))
                .andExpect(status().isNotFound());
    }
}
