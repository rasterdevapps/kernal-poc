package com.erp.kernel.resilience.backup;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BackupType}.
 */
class BackupTypeTest {

    @Test
    void shouldHaveFullValue() {
        assertThat(BackupType.FULL).isNotNull();
    }

    @Test
    void shouldHaveDifferentialValue() {
        assertThat(BackupType.DIFFERENTIAL).isNotNull();
    }

    @Test
    void shouldHaveTransactionLogValue() {
        assertThat(BackupType.TRANSACTION_LOG).isNotNull();
    }

    @Test
    void shouldParseFromString() {
        assertThat(BackupType.valueOf("FULL")).isEqualTo(BackupType.FULL);
        assertThat(BackupType.valueOf("DIFFERENTIAL")).isEqualTo(BackupType.DIFFERENTIAL);
        assertThat(BackupType.valueOf("TRANSACTION_LOG")).isEqualTo(BackupType.TRANSACTION_LOG);
    }
}
