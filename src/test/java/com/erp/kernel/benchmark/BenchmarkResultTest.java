package com.erp.kernel.benchmark;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link BenchmarkResult}.
 */
class BenchmarkResultTest {

    @Test
    void shouldCreateResult_whenAllFieldsProvided() {
        final var now = Instant.now();
        final var metrics = Map.of(BenchmarkMetric.THROUGHPUT, 1500.0);
        final var result = new BenchmarkResult("test-scenario", now, 5000L, metrics);

        assertThat(result.scenarioName()).isEqualTo("test-scenario");
        assertThat(result.executedAt()).isEqualTo(now);
        assertThat(result.durationMs()).isEqualTo(5000L);
        assertThat(result.metrics()).containsEntry(BenchmarkMetric.THROUGHPUT, 1500.0);
    }

    @Test
    void shouldCreateResult_whenMetricsMapIsEmpty() {
        final var now = Instant.now();
        final var result = new BenchmarkResult("empty-metrics", now, 100L, Map.of());

        assertThat(result.metrics()).isEmpty();
    }

    @Test
    void shouldThrowNullPointerException_whenScenarioNameIsNull() {
        final var now = Instant.now();
        final Map<BenchmarkMetric, Double> metrics = Map.of();

        assertThatThrownBy(() -> new BenchmarkResult(null, now, 100L, metrics))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("scenarioName must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenExecutedAtIsNull() {
        final Map<BenchmarkMetric, Double> metrics = Map.of();

        assertThatThrownBy(() -> new BenchmarkResult("test", null, 100L, metrics))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("executedAt must not be null");
    }

    @Test
    void shouldThrowNullPointerException_whenMetricsIsNull() {
        final var now = Instant.now();

        assertThatThrownBy(() -> new BenchmarkResult("test", now, 100L, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("metrics must not be null");
    }

    @Test
    void shouldReturnImmutableMetricsMap() {
        final var mutableMetrics = new HashMap<BenchmarkMetric, Double>();
        mutableMetrics.put(BenchmarkMetric.THROUGHPUT, 1000.0);
        final var result = new BenchmarkResult("test", Instant.now(), 100L, mutableMetrics);

        assertThatThrownBy(() -> result.metrics().put(BenchmarkMetric.ERROR_RATE, 5.0))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldNotBeAffectedByMutationOfOriginalMap() {
        final var mutableMetrics = new HashMap<BenchmarkMetric, Double>();
        mutableMetrics.put(BenchmarkMetric.THROUGHPUT, 1000.0);
        final var result = new BenchmarkResult("test", Instant.now(), 100L, mutableMetrics);

        mutableMetrics.put(BenchmarkMetric.ERROR_RATE, 5.0);

        assertThat(result.metrics()).hasSize(1);
        assertThat(result.metrics()).containsOnlyKeys(BenchmarkMetric.THROUGHPUT);
    }

    @Test
    void shouldCreateResultWithCurrentTimestamp_whenUsingOfFactory() {
        final var before = Instant.now();
        final var metrics = Map.of(BenchmarkMetric.ERROR_RATE, 0.5);
        final var result = BenchmarkResult.of("factory-test", 2000L, metrics);
        final var after = Instant.now();

        assertThat(result.scenarioName()).isEqualTo("factory-test");
        assertThat(result.durationMs()).isEqualTo(2000L);
        assertThat(result.metrics()).containsEntry(BenchmarkMetric.ERROR_RATE, 0.5);
        assertThat(result.executedAt()).isBetween(before, after);
    }

    @Test
    void shouldSupportMultipleMetrics() {
        final var metrics = Map.of(
                BenchmarkMetric.THROUGHPUT, 1500.0,
                BenchmarkMetric.RESPONSE_TIME_AVG, 25.0,
                BenchmarkMetric.ERROR_RATE, 0.1
        );
        final var result = BenchmarkResult.of("multi-metric", 3000L, metrics);

        assertThat(result.metrics()).hasSize(3);
    }

    @Test
    void shouldImplementEquality() {
        final var now = Instant.now();
        final var metrics = Map.of(BenchmarkMetric.THROUGHPUT, 100.0);
        final var result1 = new BenchmarkResult("test", now, 100L, metrics);
        final var result2 = new BenchmarkResult("test", now, 100L, metrics);

        assertThat(result1).isEqualTo(result2);
        assertThat(result1.hashCode()).isEqualTo(result2.hashCode());
    }

    @Test
    void shouldReturnReadableToString() {
        final var result = BenchmarkResult.of("to-string-test", 500L, Map.of());

        assertThat(result.toString()).contains("to-string-test");
        assertThat(result.toString()).contains("500");
    }
}
