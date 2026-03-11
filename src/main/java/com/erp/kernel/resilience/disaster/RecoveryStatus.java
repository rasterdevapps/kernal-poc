package com.erp.kernel.resilience.disaster;

/**
 * Represents the lifecycle status of a disaster recovery point.
 */
public enum RecoveryStatus {

    /** The recovery point is available and ready for use in a restore operation. */
    AVAILABLE,

    /** A restore operation from this recovery point is currently in progress. */
    RESTORING,

    /** A restore test from this recovery point completed successfully. */
    TESTED,

    /** This recovery point is expired and should no longer be used. */
    EXPIRED
}
