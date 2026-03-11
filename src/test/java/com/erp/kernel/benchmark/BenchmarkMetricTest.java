package com.erp.kernel.benchmark;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BenchmarkMetric}.
 */
class BenchmarkMetricTest {

    @Test
    void shouldHaveEightValues() {
        assertThat(BenchmarkMetric.values()).hasSize(8);
    }

    @ParameterizedTest
    @EnumSource(BenchmarkMetric.class)
    void shouldHaveNonBlankDisplayName(final BenchmarkMetric metric) {
        assertThat(metric.getDisplayName()).isNotBlank();
    }

    @Test
    void shouldReturnCorrectDisplayName_whenResponseTimeAvg() {
        assertThat(BenchmarkMetric.RESPONSE_TIME_AVG.getDisplayName())
                .isEqualTo("Response Time (avg ms)");
    }

    @Test
    void shouldReturnCorrectDisplayName_whenResponseTimeP95() {
        assertThat(BenchmarkMetric.RESPONSE_TIME_P95.getDisplayName())
                .isEqualTo("Response Time (p95 ms)");
    }

    @Test
    void shouldReturnCorrectDisplayName_whenResponseTimeP99() {
        assertThat(BenchmarkMetric.RESPONSE_TIME_P99.getDisplayName())
                .isEqualTo("Response Time (p99 ms)");
    }

    @Test
    void shouldReturnCorrectDisplayName_whenThroughput() {
        assertThat(BenchmarkMetric.THROUGHPUT.getDisplayName())
                .isEqualTo("Throughput (req/s)");
    }

    @Test
    void shouldReturnCorrectDisplayName_whenErrorRate() {
        assertThat(BenchmarkMetric.ERROR_RATE.getDisplayName())
                .isEqualTo("Error Rate (%)");
    }

    @Test
    void shouldReturnCorrectDisplayName_whenMaxConcurrentConnections() {
        assertThat(BenchmarkMetric.MAX_CONCURRENT_CONNECTIONS.getDisplayName())
                .isEqualTo("Max Concurrent Connections");
    }

    @Test
    void shouldReturnCorrectDisplayName_whenCpuUtilization() {
        assertThat(BenchmarkMetric.CPU_UTILIZATION.getDisplayName())
                .isEqualTo("CPU Utilization (%)");
    }

    @Test
    void shouldReturnCorrectDisplayName_whenMemoryUtilization() {
        assertThat(BenchmarkMetric.MEMORY_UTILIZATION.getDisplayName())
                .isEqualTo("Memory Utilization (MB)");
    }

    @Test
    void shouldResolveFromValueOf_whenValidName() {
        assertThat(BenchmarkMetric.valueOf("THROUGHPUT"))
                .isEqualTo(BenchmarkMetric.THROUGHPUT);
    }

    @Test
    void shouldResolveAllValues_whenUsingValueOf() {
        for (final var metric : BenchmarkMetric.values()) {
            assertThat(BenchmarkMetric.valueOf(metric.name())).isEqualTo(metric);
        }
    }
}
