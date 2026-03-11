package com.erp.kernel.resilience.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for observability and metrics collection.
 *
 * <p>When {@code erp.resilience.monitoring.metrics-enabled} is {@code true} (the default),
 * this configuration registers a common metric tag {@code application=erp-kernel} on
 * all Micrometer metrics, enabling consistent filtering in dashboards and alerting rules.
 */
@Configuration
@EnableConfigurationProperties(ObservabilityProperties.class)
public class ObservabilityConfiguration {

    /**
     * Registers a common {@code application} tag on all Micrometer metrics.
     *
     * <p>This tag enables multi-instance filtering in Prometheus/Grafana dashboards
     * and alerting systems.
     *
     * @return the meter registry customizer
     */
    @Bean
    @ConditionalOnProperty(
            name = "erp.resilience.monitoring.metrics-enabled",
            havingValue = "true",
            matchIfMissing = true)
    public MeterRegistryCustomizer<MeterRegistry> commonMetricsTags() {
        return registry -> registry.config().commonTags("application", "erp-kernel");
    }
}
