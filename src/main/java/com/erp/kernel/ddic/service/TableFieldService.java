package com.erp.kernel.ddic.service;

import com.erp.kernel.ddic.dto.CreateTableFieldRequest;
import com.erp.kernel.ddic.dto.TableFieldDto;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.exception.ValidationException;
import com.erp.kernel.ddic.mapper.TableFieldMapper;
import com.erp.kernel.ddic.repository.DataElementRepository;
import com.erp.kernel.ddic.repository.TableDefinitionRepository;
import com.erp.kernel.ddic.repository.TableFieldRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for DDIC table field management.
 *
 * <p>Table fields represent columns within table definitions. Extension fields
 * (prefixed with "Z_") support client-specific customisations.
 */
@Service
@Transactional(readOnly = true)
public class TableFieldService {

    private static final String EXTENSION_PREFIX = "Z_";

    private final TableFieldRepository tableFieldRepository;
    private final TableDefinitionRepository tableDefinitionRepository;
    private final DataElementRepository dataElementRepository;

    /**
     * Creates a new table field service.
     *
     * @param tableFieldRepository      the table field repository
     * @param tableDefinitionRepository the table definition repository
     * @param dataElementRepository     the data element repository
     */
    public TableFieldService(final TableFieldRepository tableFieldRepository,
                             final TableDefinitionRepository tableDefinitionRepository,
                             final DataElementRepository dataElementRepository) {
        this.tableFieldRepository = Objects.requireNonNull(tableFieldRepository,
                "tableFieldRepository must not be null");
        this.tableDefinitionRepository = Objects.requireNonNull(tableDefinitionRepository,
                "tableDefinitionRepository must not be null");
        this.dataElementRepository = Objects.requireNonNull(dataElementRepository,
                "dataElementRepository must not be null");
    }

    /**
     * Creates a new table field.
     *
     * @param request the creation request
     * @return the created table field DTO
     * @throws EntityNotFoundException  if the table definition or data element is not found
     * @throws DuplicateEntityException if a field with the same name exists in the table
     * @throws ValidationException      if an extension field name does not start with "Z_"
     */
    @Transactional
    public TableFieldDto create(final CreateTableFieldRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        validateExtensionFieldName(request);
        final var tableDefinition = tableDefinitionRepository.findById(request.tableDefinitionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Table definition with id %d not found".formatted(request.tableDefinitionId())));
        final var dataElement = dataElementRepository.findById(request.dataElementId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Data element with id %d not found".formatted(request.dataElementId())));
        if (tableFieldRepository.existsByTableDefinitionIdAndFieldName(
                request.tableDefinitionId(), request.fieldName())) {
            throw new DuplicateEntityException(
                    "Field '%s' already exists in table definition %d"
                            .formatted(request.fieldName(), request.tableDefinitionId()));
        }
        final var entity = TableFieldMapper.toEntity(request, tableDefinition, dataElement);
        final var saved = tableFieldRepository.save(entity);
        return TableFieldMapper.toDto(saved);
    }

    /**
     * Finds a table field by its ID.
     *
     * @param id the table field ID
     * @return the table field DTO
     * @throws EntityNotFoundException if the table field is not found
     */
    public TableFieldDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = tableFieldRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Table field with id %d not found".formatted(id)));
        return TableFieldMapper.toDto(entity);
    }

    /**
     * Finds all fields belonging to a specific table definition.
     *
     * @param tableDefinitionId the table definition ID
     * @return the list of table field DTOs ordered by position
     */
    public List<TableFieldDto> findByTableDefinitionId(final Long tableDefinitionId) {
        Objects.requireNonNull(tableDefinitionId, "tableDefinitionId must not be null");
        return tableFieldRepository.findByTableDefinitionIdOrderByPositionAsc(tableDefinitionId)
                .stream()
                .map(TableFieldMapper::toDto)
                .toList();
    }

    /**
     * Finds all extension fields belonging to a specific table definition.
     *
     * @param tableDefinitionId the table definition ID
     * @return the list of extension table field DTOs
     */
    public List<TableFieldDto> findExtensionFields(final Long tableDefinitionId) {
        Objects.requireNonNull(tableDefinitionId, "tableDefinitionId must not be null");
        return tableFieldRepository.findByTableDefinitionIdAndExtensionTrue(tableDefinitionId)
                .stream()
                .map(TableFieldMapper::toDto)
                .toList();
    }

    /**
     * Deletes a table field by its ID.
     *
     * @param id the table field ID
     * @throws EntityNotFoundException if the table field is not found
     */
    @Transactional
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!tableFieldRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Table field with id %d not found".formatted(id));
        }
        tableFieldRepository.deleteById(id);
    }

    private void validateExtensionFieldName(final CreateTableFieldRequest request) {
        if (request.extension() && !request.fieldName().startsWith(EXTENSION_PREFIX)) {
            throw new ValidationException(
                    "Extension field name must start with '%s', got '%s'"
                            .formatted(EXTENSION_PREFIX, request.fieldName()));
        }
    }
}
