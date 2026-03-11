package com.erp.kernel.benchmark;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for benchmark execution.
 *
 * @param enabled            whether benchmarking is enabled
 * @param warmupIterations   number of warmup iterations before measurement
 * @param measureIterations  number of measurement iterations
 * @param concurrentUsers    number of simulated concurrent users
 */
@ConfigurationProperties(prefix = "erp.benchmark")
public record BenchmarkProperties(
        boolean enabled,
        int warmupIterations,
        int measureIterations,
        int concurrentUsers
) {
}
