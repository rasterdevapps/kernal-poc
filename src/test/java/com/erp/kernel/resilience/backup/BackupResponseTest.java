package com.erp.kernel.resilience.backup;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BackupResponse}.
 */
class BackupResponseTest {

    @Test
    void shouldCreateResponse() {
        // Arrange
        final var startedAt = Instant.now();
        final var completedAt = Instant.now().plusSeconds(30);

        // Act
        final var response = new BackupResponse(
                1L, BackupType.FULL, BackupStatus.COMPLETED,
                startedAt, completedAt, "/var/backup/test.bak",
                2048L, "sha256:abc", null);

        // Assert
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.backupType()).isEqualTo(BackupType.FULL);
        assertThat(response.status()).isEqualTo(BackupStatus.COMPLETED);
        assertThat(response.startedAt()).isEqualTo(startedAt);
        assertThat(response.completedAt()).isEqualTo(completedAt);
        assertThat(response.location()).isEqualTo("/var/backup/test.bak");
        assertThat(response.sizeBytes()).isEqualTo(2048L);
        assertThat(response.checksum()).isEqualTo("sha256:abc");
        assertThat(response.errorMessage()).isNull();
    }

    @Test
    void shouldSupportEqualityAndHashCode() {
        // Arrange
        final var now = Instant.now();
        final var r1 = new BackupResponse(1L, BackupType.FULL, BackupStatus.COMPLETED,
                now, null, "/path", 100L, "chk", null);
        final var r2 = new BackupResponse(1L, BackupType.FULL, BackupStatus.COMPLETED,
                now, null, "/path", 100L, "chk", null);

        // Assert
        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}
