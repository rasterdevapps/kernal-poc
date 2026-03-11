package com.erp.kernel.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service for managing and executing benchmark scenarios.
 *
 * <p>Maintains a registry of benchmark scenarios and stores execution results.
 * Provides methods to register scenarios, execute them, and retrieve historical results.
 */
@Service
public class BenchmarkService {

    private static final Logger LOG = LoggerFactory.getLogger(BenchmarkService.class);

    private final BenchmarkProperties properties;
    private final List<BenchmarkScenario> scenarios = new CopyOnWriteArrayList<>();
    private final List<BenchmarkResult> results = new CopyOnWriteArrayList<>();

    /**
     * Creates a new benchmark service.
     *
     * @param properties the benchmark configuration properties
     */
    public BenchmarkService(final BenchmarkProperties properties) {
        this.properties = Objects.requireNonNull(
                properties, "properties must not be null");
    }

    /**
     * Registers a benchmark scenario.
     *
     * @param scenario the scenario to register
     */
    public void registerScenario(final BenchmarkScenario scenario) {
        Objects.requireNonNull(scenario, "scenario must not be null");
        scenarios.add(scenario);
        LOG.info("Registered benchmark scenario: {}", scenario.getName());
    }

    /**
     * Executes the benchmark scenario with the given name.
     *
     * @param scenarioName the name of the scenario to execute
     * @return an {@link Optional} containing the result, or empty if the scenario is not found
     */
    public Optional<BenchmarkResult> executeScenario(final String scenarioName) {
        Objects.requireNonNull(scenarioName, "scenarioName must not be null");
        if (!properties.enabled()) {
            LOG.warn("Benchmarking is disabled; skipping scenario: {}", scenarioName);
            return Optional.empty();
        }
        for (final var scenario : scenarios) {
            if (scenario.getName().equals(scenarioName)) {
                LOG.info("Executing benchmark scenario: {}", scenarioName);
                final var result = scenario.execute();
                results.add(result);
                LOG.info("Completed benchmark scenario: {} in {} ms",
                        scenarioName, result.durationMs());
                return Optional.of(result);
            }
        }
        LOG.warn("Benchmark scenario not found: {}", scenarioName);
        return Optional.empty();
    }

    /**
     * Returns all registered benchmark scenarios as an unmodifiable list.
     *
     * @return the registered scenarios
     */
    public List<BenchmarkScenario> getScenarios() {
        return Collections.unmodifiableList(scenarios);
    }

    /**
     * Returns all benchmark results as an unmodifiable list.
     *
     * @return the benchmark results
     */
    public List<BenchmarkResult> getResults() {
        return Collections.unmodifiableList(results);
    }

    /**
     * Returns the benchmark configuration properties.
     *
     * @return the properties
     */
    public BenchmarkProperties getProperties() {
        return properties;
    }

    /**
     * Removes all registered scenarios and results. Primarily for testing.
     */
    public void clear() {
        scenarios.clear();
        results.clear();
    }
}
