package com.erp.kernel.resilience.backup;

import java.time.Instant;

/**
 * Response DTO representing a backup operation record.
 *
 * @param id           the backup record identifier
 * @param backupType   the type of backup performed
 * @param status       the current lifecycle status
 * @param startedAt    when the backup operation started
 * @param completedAt  when the backup completed, or {@code null} if still in progress
 * @param location     the storage location of the backup artefact
 * @param sizeBytes    the size of the backup artefact in bytes
 * @param checksum     the integrity checksum of the backup artefact
 * @param errorMessage the error description if the backup failed
 */
public record BackupResponse(
        Long id,
        BackupType backupType,
        BackupStatus status,
        Instant startedAt,
        Instant completedAt,
        String location,
        Long sizeBytes,
        String checksum,
        String errorMessage
) {
}
