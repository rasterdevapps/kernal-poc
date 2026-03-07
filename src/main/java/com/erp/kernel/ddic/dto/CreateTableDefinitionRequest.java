package com.erp.kernel.ddic.dto;

import com.erp.kernel.ddic.model.SchemaLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a DDIC table definition.
 *
 * @param tableName       the unique table name (required, max 30 chars)
 * @param schemaLevel     the ANSI/SPARC schema level (required)
 * @param description     the description (max 255 chars)
 * @param clientSpecific  whether this table is client-specific
 */
public record CreateTableDefinitionRequest(
        @NotBlank @Size(max = 30) String tableName,
        @NotNull SchemaLevel schemaLevel,
        @Size(max = 255) String description,
        boolean clientSpecific
) {
}
