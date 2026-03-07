package com.erp.kernel.ddic.dto;

import java.time.Instant;

/**
 * Response DTO representing a DDIC search help.
 *
 * @param id                the search help ID
 * @param searchHelpName    the unique search help name
 * @param tableDefinitionId the referenced table definition ID
 * @param tableName         the referenced table name
 * @param description       the description
 * @param createdAt         the creation timestamp
 * @param updatedAt         the last update timestamp
 */
public record SearchHelpDto(
        Long id,
        String searchHelpName,
        Long tableDefinitionId,
        String tableName,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}
