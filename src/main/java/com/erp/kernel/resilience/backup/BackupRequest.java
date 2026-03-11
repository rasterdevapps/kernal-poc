package com.erp.kernel.resilience.backup;

import jakarta.validation.constraints.NotNull;

/**
 * Request body for triggering a manual backup operation.
 *
 * @param backupType the type of backup to perform
 */
public record BackupRequest(
        @NotNull BackupType backupType
) {
}
