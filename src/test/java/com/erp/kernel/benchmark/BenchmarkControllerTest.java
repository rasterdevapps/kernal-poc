package com.erp.kernel.benchmark;

import com.erp.kernel.api.config.CorsProperties;
import com.erp.kernel.api.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link BenchmarkController}.
 */
@WebMvcTest(BenchmarkController.class)
@AutoConfigureMockMvc(addFilters = false)
class BenchmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BenchmarkService benchmarkService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private CorsProperties corsProperties;

    @Test
    void shouldReturnResult_whenExecuteScenarioSucceeds() throws Exception {
        final var result = new BenchmarkResult(
                "perf-test",
                Instant.parse("2025-01-15T10:00:00Z"),
                5000L,
                Map.of(BenchmarkMetric.THROUGHPUT, 1500.0)
        );
        when(benchmarkService.executeScenario("perf-test"))
                .thenReturn(Optional.of(result));

        mockMvc.perform(post("/api/v1/benchmarks/execute")
                        .param("scenario", "perf-test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scenarioName").value("perf-test"))
                .andExpect(jsonPath("$.durationMs").value(5000))
                .andExpect(jsonPath("$.metrics.THROUGHPUT").value(1500.0));
    }

    @Test
    void shouldReturnNotFound_whenScenarioNotFound() throws Exception {
        when(benchmarkService.executeScenario("unknown"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/benchmarks/execute")
                        .param("scenario", "unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFound_whenBenchmarkingDisabled() throws Exception {
        when(benchmarkService.executeScenario("disabled-test"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/benchmarks/execute")
                        .param("scenario", "disabled-test"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnResults_whenResultsExist() throws Exception {
        final var result = new BenchmarkResult(
                "load-test",
                Instant.parse("2025-01-15T10:00:00Z"),
                3000L,
                Map.of(BenchmarkMetric.ERROR_RATE, 0.5)
        );
        when(benchmarkService.getResults()).thenReturn(List.of(result));

        mockMvc.perform(get("/api/v1/benchmarks/results"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].scenarioName").value("load-test"))
                .andExpect(jsonPath("$[0].durationMs").value(3000));
    }

    @Test
    void shouldReturnEmptyList_whenNoResultsExist() throws Exception {
        when(benchmarkService.getResults()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/benchmarks/results"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldReturnConfig() throws Exception {
        final var properties = new BenchmarkProperties(true, 3, 10, 50);
        when(benchmarkService.getProperties()).thenReturn(properties);

        mockMvc.perform(get("/api/v1/benchmarks/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.warmupIterations").value(3))
                .andExpect(jsonPath("$.measureIterations").value(10))
                .andExpect(jsonPath("$.concurrentUsers").value(50));
    }
}
