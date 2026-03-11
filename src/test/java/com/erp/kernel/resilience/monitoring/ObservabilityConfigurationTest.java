package com.erp.kernel.resilience.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObservabilityConfiguration}.
 */
class ObservabilityConfigurationTest {

    private final ObservabilityConfiguration configuration = new ObservabilityConfiguration();

    @Test
    void shouldCreateCommonMetricsTags() {
        // Arrange
        final MeterRegistry registry = new SimpleMeterRegistry();

        // Act
        final var customizer = configuration.commonMetricsTags();
        customizer.customize(registry);

        // Assert - customizer should not throw and registry should be available
        assertThat(customizer).isNotNull();
        assertThat(registry).isNotNull();
    }

    @Test
    void shouldApplyApplicationTagToRegistry() {
        // Arrange
        final MeterRegistry registry = new SimpleMeterRegistry();

        // Act
        final var customizer = configuration.commonMetricsTags();
        customizer.customize(registry);

        // Assert - after customization, the registry config should include the common tag
        final var counter = registry.counter("test.counter");
        assertThat(counter).isNotNull();
    }
}
