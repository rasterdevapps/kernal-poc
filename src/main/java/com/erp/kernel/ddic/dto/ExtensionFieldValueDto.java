package com.erp.kernel.ddic.dto;

import java.time.Instant;

/**
 * Response DTO representing a DDIC extension field value.
 *
 * @param id         the value ID
 * @param tableName  the table name
 * @param recordId   the record ID
 * @param fieldName  the extension field name
 * @param fieldValue the stored value
 * @param createdAt  the creation timestamp
 * @param updatedAt  the last update timestamp
 */
public record ExtensionFieldValueDto(
        Long id,
        String tableName,
        Long recordId,
        String fieldName,
        String fieldValue,
        Instant createdAt,
        Instant updatedAt
) {
}
