package com.erp.kernel.resilience.monitoring;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ObservabilityProperties}.
 */
class ObservabilityPropertiesTest {

    @Test
    void shouldCreatePropertiesRecord_whenMetricsEnabled() {
        // Arrange & Act
        final var properties = new ObservabilityProperties(true, false);

        // Assert
        assertThat(properties.metricsEnabled()).isTrue();
        assertThat(properties.tracingEnabled()).isFalse();
    }

    @Test
    void shouldCreatePropertiesRecord_whenTracingEnabled() {
        // Arrange & Act
        final var properties = new ObservabilityProperties(true, true);

        // Assert
        assertThat(properties.metricsEnabled()).isTrue();
        assertThat(properties.tracingEnabled()).isTrue();
    }

    @Test
    void shouldSupportEquality() {
        // Arrange
        final var p1 = new ObservabilityProperties(true, false);
        final var p2 = new ObservabilityProperties(true, false);

        // Assert
        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }
}
