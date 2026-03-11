package com.erp.kernel.resilience.disaster;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RecoveryStatus}.
 */
class RecoveryStatusTest {

    @Test
    void shouldHaveAllValues() {
        assertThat(RecoveryStatus.values()).containsExactlyInAnyOrder(
                RecoveryStatus.AVAILABLE,
                RecoveryStatus.RESTORING,
                RecoveryStatus.TESTED,
                RecoveryStatus.EXPIRED
        );
    }

    @Test
    void shouldParseFromString() {
        assertThat(RecoveryStatus.valueOf("AVAILABLE")).isEqualTo(RecoveryStatus.AVAILABLE);
        assertThat(RecoveryStatus.valueOf("RESTORING")).isEqualTo(RecoveryStatus.RESTORING);
        assertThat(RecoveryStatus.valueOf("TESTED")).isEqualTo(RecoveryStatus.TESTED);
        assertThat(RecoveryStatus.valueOf("EXPIRED")).isEqualTo(RecoveryStatus.EXPIRED);
    }
}
