package com.erp.kernel.resilience.backup;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the automated backup strategy.
 *
 * @param enabled       whether the backup system is enabled
 * @param retentionDays number of days to retain backup artefacts before pruning
 * @param storagePath   the file system path where backup artefacts are stored
 */
@ConfigurationProperties(prefix = "erp.resilience.backup")
public record BackupProperties(
        boolean enabled,
        int retentionDays,
        String storagePath
) {
}
