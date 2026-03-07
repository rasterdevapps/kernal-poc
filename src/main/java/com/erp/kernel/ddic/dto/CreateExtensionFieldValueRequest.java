package com.erp.kernel.ddic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a DDIC extension field value.
 *
 * @param tableName  the table name (required, max 30 chars)
 * @param recordId   the record ID (required)
 * @param fieldName  the extension field name (required, max 30 chars, must start with "Z_")
 * @param fieldValue the value to store (max 1024 chars)
 */
public record CreateExtensionFieldValueRequest(
        @NotBlank @Size(max = 30) String tableName,
        @NotNull Long recordId,
        @NotBlank @Size(max = 30) String fieldName,
        @Size(max = 1024) String fieldValue
) {
}
