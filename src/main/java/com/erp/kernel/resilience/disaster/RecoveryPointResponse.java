package com.erp.kernel.resilience.disaster;

import java.time.Instant;

/**
 * Response DTO representing a disaster recovery point.
 *
 * @param id                the recovery point identifier
 * @param backupRecordId    the identifier of the underlying backup record
 * @param label             a human-readable label
 * @param status            the current lifecycle status
 * @param capturedAt        when the snapshot was captured
 * @param replicatedAt      when it was replicated off-site, or {@code null} if not yet replicated
 * @param replicationTarget the off-site replication target host or URL
 * @param notes             optional descriptive notes
 */
public record RecoveryPointResponse(
        Long id,
        Long backupRecordId,
        String label,
        RecoveryStatus status,
        Instant capturedAt,
        Instant replicatedAt,
        String replicationTarget,
        String notes
) {
}
