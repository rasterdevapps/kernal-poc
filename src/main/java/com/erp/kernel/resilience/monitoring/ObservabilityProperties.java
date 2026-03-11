package com.erp.kernel.resilience.monitoring;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for observability features.
 *
 * @param metricsEnabled whether Micrometer metrics collection is enabled
 * @param tracingEnabled whether distributed tracing is enabled
 */
@ConfigurationProperties(prefix = "erp.resilience.monitoring")
public record ObservabilityProperties(
        boolean metricsEnabled,
        boolean tracingEnabled
) {
}
