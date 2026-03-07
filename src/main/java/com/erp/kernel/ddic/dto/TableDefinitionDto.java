package com.erp.kernel.ddic.dto;

import com.erp.kernel.ddic.model.SchemaLevel;

import java.time.Instant;

/**
 * Response DTO representing a DDIC table definition.
 *
 * @param id              the table definition ID
 * @param tableName       the unique table name
 * @param schemaLevel     the ANSI/SPARC schema level
 * @param description     the description
 * @param clientSpecific  whether this table is client-specific
 * @param createdAt       the creation timestamp
 * @param updatedAt       the last update timestamp
 */
public record TableDefinitionDto(
        Long id,
        String tableName,
        SchemaLevel schemaLevel,
        String description,
        boolean clientSpecific,
        Instant createdAt,
        Instant updatedAt
) {
}
