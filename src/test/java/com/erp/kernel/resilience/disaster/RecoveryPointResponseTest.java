package com.erp.kernel.resilience.disaster;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RecoveryPointResponse}.
 */
class RecoveryPointResponseTest {

    @Test
    void shouldCreateResponse() {
        // Arrange
        final var capturedAt = Instant.now();
        final var replicatedAt = Instant.now().plusSeconds(60);

        // Act
        final var response = new RecoveryPointResponse(
                1L, 10L, "Snapshot A", RecoveryStatus.AVAILABLE,
                capturedAt, replicatedAt, "dr-site.example.com", "Initial snapshot");

        // Assert
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.backupRecordId()).isEqualTo(10L);
        assertThat(response.label()).isEqualTo("Snapshot A");
        assertThat(response.status()).isEqualTo(RecoveryStatus.AVAILABLE);
        assertThat(response.capturedAt()).isEqualTo(capturedAt);
        assertThat(response.replicatedAt()).isEqualTo(replicatedAt);
        assertThat(response.replicationTarget()).isEqualTo("dr-site.example.com");
        assertThat(response.notes()).isEqualTo("Initial snapshot");
    }

    @Test
    void shouldSupportEqualityAndHashCode() {
        // Arrange
        final var now = Instant.now();
        final var r1 = new RecoveryPointResponse(1L, 2L, "L", RecoveryStatus.TESTED,
                now, null, "host", "notes");
        final var r2 = new RecoveryPointResponse(1L, 2L, "L", RecoveryStatus.TESTED,
                now, null, "host", "notes");

        // Assert
        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}
