package com.erp.kernel.resilience.disaster;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RecoveryPoint}.
 */
class RecoveryPointTest {

    @Test
    void shouldSetAndGetAllFields() {
        // Arrange
        final var point = new RecoveryPoint();
        final var capturedAt = Instant.now();
        final var replicatedAt = Instant.now().plusSeconds(30);

        // Act
        point.setBackupRecordId(10L);
        point.setLabel("Pre-deployment snapshot");
        point.setStatus(RecoveryStatus.AVAILABLE);
        point.setCapturedAt(capturedAt);
        point.setReplicatedAt(replicatedAt);
        point.setReplicationTarget("dr-site.example.com");
        point.setNotes("Captured before v2 deployment");

        // Assert
        assertThat(point.getBackupRecordId()).isEqualTo(10L);
        assertThat(point.getLabel()).isEqualTo("Pre-deployment snapshot");
        assertThat(point.getStatus()).isEqualTo(RecoveryStatus.AVAILABLE);
        assertThat(point.getCapturedAt()).isEqualTo(capturedAt);
        assertThat(point.getReplicatedAt()).isEqualTo(replicatedAt);
        assertThat(point.getReplicationTarget()).isEqualTo("dr-site.example.com");
        assertThat(point.getNotes()).isEqualTo("Captured before v2 deployment");
    }

    @Test
    void shouldHaveNullFieldsByDefault() {
        // Act
        final var point = new RecoveryPoint();

        // Assert
        assertThat(point.getBackupRecordId()).isNull();
        assertThat(point.getLabel()).isNull();
        assertThat(point.getStatus()).isNull();
        assertThat(point.getCapturedAt()).isNull();
        assertThat(point.getReplicatedAt()).isNull();
        assertThat(point.getReplicationTarget()).isNull();
        assertThat(point.getNotes()).isNull();
    }
}
