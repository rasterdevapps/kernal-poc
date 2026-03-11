package com.erp.kernel.benchmark;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable result of a benchmark execution.
 *
 * <p>Captures the scenario name, execution timestamp, duration,
 * and all measured metric values.
 *
 * @param scenarioName the name of the benchmark scenario
 * @param executedAt   the instant the benchmark was executed
 * @param durationMs   the total duration of the benchmark in milliseconds
 * @param metrics      the measured metric values
 */
public record BenchmarkResult(
        String scenarioName,
        Instant executedAt,
        long durationMs,
        Map<BenchmarkMetric, Double> metrics
) {

    /**
     * Creates a validated benchmark result.
     *
     * @param scenarioName the scenario name (required)
     * @param executedAt   the execution timestamp (required)
     * @param durationMs   the duration in milliseconds
     * @param metrics      the metric values (required, may be empty)
     */
    public BenchmarkResult {
        Objects.requireNonNull(scenarioName, "scenarioName must not be null");
        Objects.requireNonNull(executedAt, "executedAt must not be null");
        Objects.requireNonNull(metrics, "metrics must not be null");
        metrics = Map.copyOf(metrics);
    }

    /**
     * Creates a benchmark result with the current timestamp.
     *
     * @param scenarioName the scenario name
     * @param durationMs   the duration in milliseconds
     * @param metrics      the metric values
     * @return a new benchmark result
     */
    public static BenchmarkResult of(final String scenarioName,
                                     final long durationMs,
                                     final Map<BenchmarkMetric, Double> metrics) {
        return new BenchmarkResult(scenarioName, Instant.now(), durationMs, metrics);
    }
}
