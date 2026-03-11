package com.erp.kernel.resilience.disaster;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties defining disaster recovery targets and parameters.
 *
 * @param rpoMinutes        the Recovery Point Objective in minutes (maximum acceptable data loss)
 * @param rtoMinutes        the Recovery Time Objective in minutes (maximum acceptable downtime)
 * @param replicationTarget the host or URL of the off-site replication target
 */
@ConfigurationProperties(prefix = "erp.resilience.disaster-recovery")
public record DisasterRecoveryProperties(
        int rpoMinutes,
        int rtoMinutes,
        String replicationTarget
) {
}
