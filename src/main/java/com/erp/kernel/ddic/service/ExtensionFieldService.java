package com.erp.kernel.ddic.service;

import com.erp.kernel.ddic.dto.CreateExtensionFieldValueRequest;
import com.erp.kernel.ddic.dto.ExtensionFieldValueDto;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.exception.ValidationException;
import com.erp.kernel.ddic.mapper.ExtensionFieldValueMapper;
import com.erp.kernel.ddic.repository.ExtensionFieldValueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for client-specific extension field values.
 *
 * <p>Extension fields allow clients to add custom "Z" fields to standard tables
 * without altering the core schema, using the EAV (Entity-Attribute-Value) pattern.
 */
@Service
@Transactional(readOnly = true)
public class ExtensionFieldService {

    private static final String EXTENSION_PREFIX = "Z_";

    private final ExtensionFieldValueRepository extensionFieldValueRepository;

    /**
     * Creates a new extension field service.
     *
     * @param extensionFieldValueRepository the extension field value repository
     */
    public ExtensionFieldService(final ExtensionFieldValueRepository extensionFieldValueRepository) {
        this.extensionFieldValueRepository = Objects.requireNonNull(extensionFieldValueRepository,
                "extensionFieldValueRepository must not be null");
    }

    /**
     * Creates or updates an extension field value.
     *
     * @param request the creation request
     * @return the created or updated extension field value DTO
     * @throws ValidationException if the field name does not start with "Z_"
     */
    @Transactional
    public ExtensionFieldValueDto save(final CreateExtensionFieldValueRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        validateFieldName(request.fieldName());
        final var entity = extensionFieldValueRepository
                .findByTableNameAndRecordIdAndFieldName(
                        request.tableName(), request.recordId(), request.fieldName())
                .map(existing -> {
                    ExtensionFieldValueMapper.updateEntity(existing, request);
                    return existing;
                })
                .orElseGet(() -> ExtensionFieldValueMapper.toEntity(request));
        final var saved = extensionFieldValueRepository.save(entity);
        return ExtensionFieldValueMapper.toDto(saved);
    }

    /**
     * Finds all extension field values for a specific record.
     *
     * @param tableName the table name
     * @param recordId  the record ID
     * @return the list of extension field value DTOs
     */
    public List<ExtensionFieldValueDto> findByRecord(final String tableName, final Long recordId) {
        Objects.requireNonNull(tableName, "tableName must not be null");
        Objects.requireNonNull(recordId, "recordId must not be null");
        return extensionFieldValueRepository.findByTableNameAndRecordId(tableName, recordId)
                .stream()
                .map(ExtensionFieldValueMapper::toDto)
                .toList();
    }

    /**
     * Deletes an extension field value by its ID.
     *
     * @param id the extension field value ID
     * @throws EntityNotFoundException if the value is not found
     */
    @Transactional
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!extensionFieldValueRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Extension field value with id %d not found".formatted(id));
        }
        extensionFieldValueRepository.deleteById(id);
    }

    private void validateFieldName(final String fieldName) {
        if (!fieldName.startsWith(EXTENSION_PREFIX)) {
            throw new ValidationException(
                    "Extension field name must start with '%s', got '%s'"
                            .formatted(EXTENSION_PREFIX, fieldName));
        }
    }
}
