package com.erp.kernel.resilience.disaster;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RecoveryTargetResponse}.
 */
class RecoveryTargetResponseTest {

    @Test
    void shouldCreateResponse() {
        // Arrange & Act
        final var response = new RecoveryTargetResponse(60, 240, "dr-site.example.com");

        // Assert
        assertThat(response.rpoMinutes()).isEqualTo(60);
        assertThat(response.rtoMinutes()).isEqualTo(240);
        assertThat(response.replicationTarget()).isEqualTo("dr-site.example.com");
    }

    @Test
    void shouldSupportEqualityAndHashCode() {
        // Arrange
        final var r1 = new RecoveryTargetResponse(30, 120, "host");
        final var r2 = new RecoveryTargetResponse(30, 120, "host");

        // Assert
        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}
