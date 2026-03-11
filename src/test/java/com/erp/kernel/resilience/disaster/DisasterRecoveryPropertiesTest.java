package com.erp.kernel.resilience.disaster;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DisasterRecoveryProperties}.
 */
class DisasterRecoveryPropertiesTest {

    @Test
    void shouldCreatePropertiesRecord() {
        // Arrange & Act
        final var properties = new DisasterRecoveryProperties(60, 240, "dr-site.example.com");

        // Assert
        assertThat(properties.rpoMinutes()).isEqualTo(60);
        assertThat(properties.rtoMinutes()).isEqualTo(240);
        assertThat(properties.replicationTarget()).isEqualTo("dr-site.example.com");
    }

    @Test
    void shouldSupportEmptyReplicationTarget() {
        // Arrange & Act
        final var properties = new DisasterRecoveryProperties(30, 120, "");

        // Assert
        assertThat(properties.replicationTarget()).isEmpty();
    }
}
