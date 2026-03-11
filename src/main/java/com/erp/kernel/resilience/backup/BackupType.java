package com.erp.kernel.resilience.backup;

/**
 * Represents the type of database backup operation.
 */
public enum BackupType {

    /** A complete copy of all database data. */
    FULL,

    /** Only data changed since the last full backup. */
    DIFFERENTIAL,

    /** All transaction log entries since the last backup. */
    TRANSACTION_LOG
}
