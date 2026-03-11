package com.erp.kernel.resilience.backup;

/**
 * Represents the lifecycle status of a backup operation.
 */
public enum BackupStatus {

    /** Backup is queued and waiting to start. */
    PENDING,

    /** Backup is currently executing. */
    IN_PROGRESS,

    /** Backup completed successfully and the artefact is available. */
    COMPLETED,

    /** Backup failed due to an error; see the error message for details. */
    FAILED,

    /** Backup has been integrity-verified and is safe for restoration. */
    VERIFIED
}
