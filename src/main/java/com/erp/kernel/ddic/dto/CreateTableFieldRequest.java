package com.erp.kernel.ddic.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a DDIC table field.
 *
 * @param tableDefinitionId the parent table definition ID (required)
 * @param fieldName         the field name (required, max 30 chars)
 * @param dataElementId     the data element ID (required)
 * @param position          the ordinal position (required, minimum 1)
 * @param key               whether this is a key field
 * @param nullable          whether this field allows null
 * @param extension         whether this is an extension ("Z") field
 */
public record CreateTableFieldRequest(
        @NotNull Long tableDefinitionId,
        @NotBlank @Size(max = 30) String fieldName,
        @NotNull Long dataElementId,
        @Min(1) int position,
        boolean key,
        boolean nullable,
        boolean extension
) {
}
