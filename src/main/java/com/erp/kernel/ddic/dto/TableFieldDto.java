package com.erp.kernel.ddic.dto;

import java.time.Instant;

/**
 * Response DTO representing a DDIC table field.
 *
 * @param id                the field ID
 * @param tableDefinitionId the parent table definition ID
 * @param fieldName         the field name
 * @param dataElementId     the referenced data element ID
 * @param dataElementName   the referenced data element name
 * @param position          the ordinal position
 * @param key               whether this is a key field
 * @param nullable          whether this field allows null
 * @param extension         whether this is an extension ("Z") field
 * @param createdAt         the creation timestamp
 * @param updatedAt         the last update timestamp
 */
public record TableFieldDto(
        Long id,
        Long tableDefinitionId,
        String fieldName,
        Long dataElementId,
        String dataElementName,
        int position,
        boolean key,
        boolean nullable,
        boolean extension,
        Instant createdAt,
        Instant updatedAt
) {
}
