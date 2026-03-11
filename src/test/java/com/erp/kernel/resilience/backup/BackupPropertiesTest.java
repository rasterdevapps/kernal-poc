package com.erp.kernel.resilience.backup;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BackupProperties}.
 */
class BackupPropertiesTest {

    @Test
    void shouldCreatePropertiesRecord() {
        // Arrange & Act
        final var properties = new BackupProperties(true, 30, "/var/erp/backups");

        // Assert
        assertThat(properties.enabled()).isTrue();
        assertThat(properties.retentionDays()).isEqualTo(30);
        assertThat(properties.storagePath()).isEqualTo("/var/erp/backups");
    }

    @Test
    void shouldSupportDisabledMode() {
        // Arrange & Act
        final var properties = new BackupProperties(false, 7, "/tmp/backups");

        // Assert
        assertThat(properties.enabled()).isFalse();
        assertThat(properties.retentionDays()).isEqualTo(7);
        assertThat(properties.storagePath()).isEqualTo("/tmp/backups");
    }
}
