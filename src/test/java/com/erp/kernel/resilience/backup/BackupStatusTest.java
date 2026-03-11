package com.erp.kernel.resilience.backup;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BackupStatus}.
 */
class BackupStatusTest {

    @Test
    void shouldHaveAllValues() {
        assertThat(BackupStatus.values()).containsExactlyInAnyOrder(
                BackupStatus.PENDING,
                BackupStatus.IN_PROGRESS,
                BackupStatus.COMPLETED,
                BackupStatus.FAILED,
                BackupStatus.VERIFIED
        );
    }

    @Test
    void shouldParseFromString() {
        assertThat(BackupStatus.valueOf("PENDING")).isEqualTo(BackupStatus.PENDING);
        assertThat(BackupStatus.valueOf("IN_PROGRESS")).isEqualTo(BackupStatus.IN_PROGRESS);
        assertThat(BackupStatus.valueOf("COMPLETED")).isEqualTo(BackupStatus.COMPLETED);
        assertThat(BackupStatus.valueOf("FAILED")).isEqualTo(BackupStatus.FAILED);
        assertThat(BackupStatus.valueOf("VERIFIED")).isEqualTo(BackupStatus.VERIFIED);
    }
}
