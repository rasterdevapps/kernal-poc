package com.erp.kernel.resilience.disaster;

/**
 * Response DTO describing the configured disaster recovery targets.
 *
 * @param rpoMinutes        the Recovery Point Objective in minutes
 * @param rtoMinutes        the Recovery Time Objective in minutes
 * @param replicationTarget the off-site replication target host or URL
 */
public record RecoveryTargetResponse(
        int rpoMinutes,
        int rtoMinutes,
        String replicationTarget
) {
}
