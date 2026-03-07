package com.erp.kernel.ddic.mapper;

import com.erp.kernel.ddic.dto.CreateExtensionFieldValueRequest;
import com.erp.kernel.ddic.dto.ExtensionFieldValueDto;
import com.erp.kernel.ddic.entity.ExtensionFieldValue;

import java.util.Objects;

/**
 * Maps between {@link ExtensionFieldValue} entities and their DTOs.
 */
public final class ExtensionFieldValueMapper {

    private ExtensionFieldValueMapper() {
    }

    /**
     * Converts an extension field value entity to its response DTO.
     *
     * @param entity the extension field value entity
     * @return the extension field value DTO
     */
    public static ExtensionFieldValueDto toDto(final ExtensionFieldValue entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        return new ExtensionFieldValueDto(
                entity.getId(),
                entity.getTableName(),
                entity.getRecordId(),
                entity.getFieldName(),
                entity.getFieldValue(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converts a create request to a new extension field value entity.
     *
     * @param request the create request
     * @return a new extension field value entity (not yet persisted)
     */
    public static ExtensionFieldValue toEntity(final CreateExtensionFieldValueRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var value = new ExtensionFieldValue();
        value.setTableName(request.tableName());
        value.setRecordId(request.recordId());
        value.setFieldName(request.fieldName());
        value.setFieldValue(request.fieldValue());
        return value;
    }

    /**
     * Updates an existing extension field value entity with values from the request.
     *
     * @param entity  the entity to update
     * @param request the update request
     */
    public static void updateEntity(final ExtensionFieldValue entity,
                                    final CreateExtensionFieldValueRequest request) {
        Objects.requireNonNull(entity, "entity must not be null");
        Objects.requireNonNull(request, "request must not be null");
        entity.setTableName(request.tableName());
        entity.setRecordId(request.recordId());
        entity.setFieldName(request.fieldName());
        entity.setFieldValue(request.fieldValue());
    }
}
