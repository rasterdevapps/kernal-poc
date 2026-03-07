package com.erp.kernel.ddic.mapper;

import com.erp.kernel.ddic.dto.CreateTableDefinitionRequest;
import com.erp.kernel.ddic.dto.TableDefinitionDto;
import com.erp.kernel.ddic.entity.TableDefinition;

import java.util.Objects;

/**
 * Maps between {@link TableDefinition} entities and their DTOs.
 */
public final class TableDefinitionMapper {

    private TableDefinitionMapper() {
    }

    /**
     * Converts a table definition entity to its response DTO.
     *
     * @param entity the table definition entity
     * @return the table definition DTO
     */
    public static TableDefinitionDto toDto(final TableDefinition entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new TableDefinitionDto(
                entity.getId(),
                entity.getTableName(),
                entity.getSchemaLevel(),
                entity.getDescription(),
                entity.isClientSpecific(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new table definition entity.
     *
     * @param request the create request
     * @return a new table definition entity (not yet persisted)
     */
    public static TableDefinition toEntity(final CreateTableDefinitionRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var table = new TableDefinition();
        table.setTableName(request.tableName());
        table.setSchemaLevel(request.schemaLevel());
        table.setDescription(request.description());
        table.setClientSpecific(request.clientSpecific());
        return table;
    }

    /**
     * Updates an existing table definition entity with values from the request.
     *
     * @param entity  the entity to update
     * @param request the update request
     */
    public static void updateEntity(final TableDefinition entity,
                                    final CreateTableDefinitionRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setTableName(request.tableName());
        entity.setSchemaLevel(request.schemaLevel());
        entity.setDescription(request.description());
        entity.setClientSpecific(request.clientSpecific());
    }
}
