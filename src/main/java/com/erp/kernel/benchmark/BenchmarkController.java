package com.erp.kernel.benchmark;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * REST controller for benchmark operations.
 *
 * <p>Provides endpoints for executing benchmark scenarios and retrieving
 * results at {@code /api/v1/benchmarks}.
 */
@RestController
@RequestMapping("/api/v1/benchmarks")
public class BenchmarkController {

    private final BenchmarkService benchmarkService;

    /**
     * Creates a new benchmark controller.
     *
     * @param benchmarkService the benchmark service
     */
    public BenchmarkController(final BenchmarkService benchmarkService) {
        this.benchmarkService = Objects.requireNonNull(
                benchmarkService, "benchmarkService must not be null");
    }

    /**
     * Executes a benchmark scenario by name.
     *
     * @param scenario the scenario name
     * @return the benchmark result, or 404 if the scenario is not found or disabled
     */
    @PostMapping("/execute")
    public ResponseEntity<BenchmarkResult> execute(
            @RequestParam final String scenario) {
        return benchmarkService.executeScenario(scenario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Returns all benchmark execution results.
     *
     * @return the benchmark results
     */
    @GetMapping("/results")
    public ResponseEntity<List<BenchmarkResult>> getResults() {
        return ResponseEntity.ok(benchmarkService.getResults());
    }

    /**
     * Returns the benchmark configuration.
     *
     * @return the benchmark properties
     */
    @GetMapping("/config")
    public ResponseEntity<BenchmarkProperties> getConfig() {
        return ResponseEntity.ok(benchmarkService.getProperties());
    }
}
