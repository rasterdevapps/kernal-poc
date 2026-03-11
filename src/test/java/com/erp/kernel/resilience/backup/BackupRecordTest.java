package com.erp.kernel.resilience.backup;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BackupRecord}.
 */
class BackupRecordTest {

    @Test
    void shouldSetAndGetAllFields() {
        // Arrange
        final var record = new BackupRecord();
        final var startedAt = Instant.now();
        final var completedAt = Instant.now().plusSeconds(60);

        // Act
        record.setBackupType(BackupType.FULL);
        record.setStatus(BackupStatus.COMPLETED);
        record.setStartedAt(startedAt);
        record.setCompletedAt(completedAt);
        record.setLocation("/var/erp/backups/test.bak");
        record.setSizeBytes(1024L);
        record.setChecksum("abc123");
        record.setErrorMessage("some error");

        // Assert
        assertThat(record.getBackupType()).isEqualTo(BackupType.FULL);
        assertThat(record.getStatus()).isEqualTo(BackupStatus.COMPLETED);
        assertThat(record.getStartedAt()).isEqualTo(startedAt);
        assertThat(record.getCompletedAt()).isEqualTo(completedAt);
        assertThat(record.getLocation()).isEqualTo("/var/erp/backups/test.bak");
        assertThat(record.getSizeBytes()).isEqualTo(1024L);
        assertThat(record.getChecksum()).isEqualTo("abc123");
        assertThat(record.getErrorMessage()).isEqualTo("some error");
    }

    @Test
    void shouldHaveNullFieldsByDefault() {
        // Act
        final var record = new BackupRecord();

        // Assert
        assertThat(record.getBackupType()).isNull();
        assertThat(record.getStatus()).isNull();
        assertThat(record.getStartedAt()).isNull();
        assertThat(record.getCompletedAt()).isNull();
        assertThat(record.getLocation()).isNull();
        assertThat(record.getSizeBytes()).isNull();
        assertThat(record.getChecksum()).isNull();
        assertThat(record.getErrorMessage()).isNull();
    }
}
