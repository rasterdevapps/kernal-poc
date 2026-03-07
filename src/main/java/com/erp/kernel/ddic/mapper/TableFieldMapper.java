package com.erp.kernel.ddic.mapper;

import com.erp.kernel.ddic.dto.CreateTableFieldRequest;
import com.erp.kernel.ddic.dto.TableFieldDto;
import com.erp.kernel.ddic.entity.DataElement;
import com.erp.kernel.ddic.entity.TableDefinition;
import com.erp.kernel.ddic.entity.TableField;

import java.util.Objects;

/**
 * Maps between {@link TableField} entities and their DTOs.
 */
public final class TableFieldMapper {

    private TableFieldMapper() {
    }

    /**
     * Converts a table field entity to its response DTO.
     *
     * @param entity the table field entity
     * @return the table field DTO
     */
    public static TableFieldDto toDto(final TableField entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new TableFieldDto(
                entity.getId(),
                entity.getTableDefinition().getId(),
                entity.getFieldName(),
                entity.getDataElement().getId(),
                entity.getDataElement().getElementName(),
                entity.getPosition(),
                entity.isKey(),
                entity.isNullable(),
                entity.isExtension(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new table field entity.
     *
     * @param request         the create request
     * @param tableDefinition the parent table definition
     * @param dataElement     the referenced data element
     * @return a new table field entity (not yet persisted)
     */
    public static TableField toEntity(final CreateTableFieldRequest request,
                                      final TableDefinition tableDefinition,
                                      final DataElement dataElement) {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(tableDefinition, "tableDefinition must not be null");
        Objects.requireNonNull(dataElement, "dataElement must not be null");
        final var field = new TableField();
        field.setTableDefinition(tableDefinition);
        field.setFieldName(request.fieldName());
        field.setDataElement(dataElement);
        field.setPosition(request.position());
        field.setKey(request.key());
        field.setNullable(request.nullable());
        field.setExtension(request.extension());
        return field;
    }

    /**
     * Updates an existing table field entity with values from the request.
     *
     * @param entity      the entity to update
     * @param request     the update request
     * @param tableDefinition the parent table definition
     * @param dataElement the referenced data element
     */
    public static void updateEntity(final TableField entity,
                                    final CreateTableFieldRequest request,
                                    final TableDefinition tableDefinition,
                                    final DataElement dataElement) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(tableDefinition, "tableDefinition must not be null");
        Objects.requireNonNull(dataElement, "dataElement must not be null");
        entity.setTableDefinition(tableDefinition);
        entity.setFieldName(request.fieldName());
        entity.setDataElement(dataElement);
        entity.setPosition(request.position());
        entity.setKey(request.key());
        entity.setNullable(request.nullable());
        entity.setExtension(request.extension());
    }
}
