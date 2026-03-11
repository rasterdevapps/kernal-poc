package com.erp.kernel.benchmark;

/**
 * Defines a benchmark scenario that can be executed by the benchmark service.
 *
 * <p>Each scenario targets a specific system capability (e.g., API throughput,
 * database query performance, cache hit rates) and produces measurable results.
 */
public interface BenchmarkScenario {

    /**
     * Returns the unique name of this scenario.
     *
     * @return the scenario name
     */
    String getName();

    /**
     * Returns a description of what this scenario measures.
     *
     * @return the scenario description
     */
    String getDescription();

    /**
     * Executes the benchmark scenario and returns the result.
     *
     * @return the benchmark result
     */
    BenchmarkResult execute();
}
