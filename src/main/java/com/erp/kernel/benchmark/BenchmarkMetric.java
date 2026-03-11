package com.erp.kernel.benchmark;

/**
 * Enumerates the metrics measured during performance benchmarking.
 */
public enum BenchmarkMetric {

    /** Average response time in milliseconds. */
    RESPONSE_TIME_AVG("Response Time (avg ms)"),

    /** 95th percentile response time in milliseconds. */
    RESPONSE_TIME_P95("Response Time (p95 ms)"),

    /** 99th percentile response time in milliseconds. */
    RESPONSE_TIME_P99("Response Time (p99 ms)"),

    /** Number of requests processed per second. */
    THROUGHPUT("Throughput (req/s)"),

    /** Percentage of failed requests. */
    ERROR_RATE("Error Rate (%)"),

    /** Maximum concurrent connections handled. */
    MAX_CONCURRENT_CONNECTIONS("Max Concurrent Connections"),

    /** Average CPU utilization percentage. */
    CPU_UTILIZATION("CPU Utilization (%)"),

    /** Average memory utilization in megabytes. */
    MEMORY_UTILIZATION("Memory Utilization (MB)");

    private final String displayName;

    BenchmarkMetric(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable display name of this metric.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
