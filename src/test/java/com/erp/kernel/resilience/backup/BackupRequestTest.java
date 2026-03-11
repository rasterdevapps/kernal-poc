package com.erp.kernel.resilience.backup;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BackupRequest}.
 */
class BackupRequestTest {

    @Test
    void shouldCreateRequest() {
        // Arrange & Act
        final var request = new BackupRequest(BackupType.FULL);

        // Assert
        assertThat(request.backupType()).isEqualTo(BackupType.FULL);
    }

    @Test
    void shouldSupportEquality() {
        // Arrange
        final var request1 = new BackupRequest(BackupType.DIFFERENTIAL);
        final var request2 = new BackupRequest(BackupType.DIFFERENTIAL);

        // Assert
        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    void shouldSupportToString() {
        // Arrange
        final var request = new BackupRequest(BackupType.TRANSACTION_LOG);

        // Assert
        assertThat(request.toString()).contains("TRANSACTION_LOG");
    }
}
