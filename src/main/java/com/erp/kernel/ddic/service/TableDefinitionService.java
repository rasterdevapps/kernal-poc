package com.erp.kernel.ddic.service;

import com.erp.kernel.ddic.dto.CreateTableDefinitionRequest;
import com.erp.kernel.ddic.dto.TableDefinitionDto;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.mapper.TableDefinitionMapper;
import com.erp.kernel.ddic.model.SchemaLevel;
import com.erp.kernel.ddic.repository.TableDefinitionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for DDIC table definition management.
 *
 * <p>Table definitions represent the ANSI/SPARC three-schema architecture,
 * where each table is classified at the External, Conceptual, or Internal level.
 */
@Service
@Transactional(readOnly = true)
public class TableDefinitionService {

    private final TableDefinitionRepository tableDefinitionRepository;

    /**
     * Creates a new table definition service.
     *
     * @param tableDefinitionRepository the table definition repository
     */
    public TableDefinitionService(final TableDefinitionRepository tableDefinitionRepository) {
        this.tableDefinitionRepository = Objects.requireNonNull(tableDefinitionRepository,
                "tableDefinitionRepository must not be null");
    }

    /**
     * Creates a new table definition.
     *
     * @param request the creation request
     * @return the created table definition DTO
     * @throws DuplicateEntityException if a table with the same name exists
     */
    @Transactional
    @CacheEvict(value = "tableDefinitions", allEntries = true)
    public TableDefinitionDto create(final CreateTableDefinitionRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (tableDefinitionRepository.existsByTableName(request.tableName())) {
            throw new DuplicateEntityException(
                    "Table definition with name '%s' already exists".formatted(request.tableName()));
        }
        final var entity = TableDefinitionMapper.toEntity(request);
        final var saved = tableDefinitionRepository.save(entity);
        return TableDefinitionMapper.toDto(saved);
    }

    /**
     * Finds a table definition by its ID.
     *
     * @param id the table definition ID
     * @return the table definition DTO
     * @throws EntityNotFoundException if the table definition is not found
     */
    @Cacheable(value = "tableDefinitions", key = "#id")
    public TableDefinitionDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = tableDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Table definition with id %d not found".formatted(id)));
        return TableDefinitionMapper.toDto(entity);
    }

    /**
     * Returns all table definitions.
     *
     * @return the list of all table definition DTOs
     */
    @Cacheable(value = "tableDefinitions", key = "'all'")
    public List<TableDefinitionDto> findAll() {
        return tableDefinitionRepository.findAll().stream()
                .map(TableDefinitionMapper::toDto)
                .toList();
    }

    /**
     * Finds all table definitions with the given schema level.
     *
     * @param schemaLevel the schema level to filter by
     * @return the list of matching table definition DTOs
     */
    @Cacheable(value = "tableDefinitions", key = "#schemaLevel")
    public List<TableDefinitionDto> findBySchemaLevel(final SchemaLevel schemaLevel) {
        Objects.requireNonNull(schemaLevel, "schemaLevel must not be null");
        return tableDefinitionRepository.findBySchemaLevel(schemaLevel).stream()
                .map(TableDefinitionMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing table definition.
     *
     * @param id      the table definition ID
     * @param request the update request
     * @return the updated table definition DTO
     * @throws EntityNotFoundException  if the table definition is not found
     * @throws DuplicateEntityException if the new name conflicts with another table
     */
    @Transactional
    @CacheEvict(value = "tableDefinitions", allEntries = true)
    public TableDefinitionDto update(final Long id, final CreateTableDefinitionRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = tableDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Table definition with id %d not found".formatted(id)));
        tableDefinitionRepository.findByTableName(request.tableName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "Table definition with name '%s' already exists"
                                    .formatted(request.tableName()));
                });
        TableDefinitionMapper.updateEntity(entity, request);
        final var saved = tableDefinitionRepository.save(entity);
        return TableDefinitionMapper.toDto(saved);
    }

    /**
     * Deletes a table definition by its ID.
     *
     * @param id the table definition ID
     * @throws EntityNotFoundException if the table definition is not found
     */
    @Transactional
    @CacheEvict(value = "tableDefinitions", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!tableDefinitionRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Table definition with id %d not found".formatted(id));
        }
        tableDefinitionRepository.deleteById(id);
    }
}
