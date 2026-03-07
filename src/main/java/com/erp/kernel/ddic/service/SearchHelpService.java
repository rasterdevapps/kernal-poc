package com.erp.kernel.ddic.service;

import com.erp.kernel.ddic.dto.CreateSearchHelpRequest;
import com.erp.kernel.ddic.dto.SearchHelpDto;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.mapper.SearchHelpMapper;
import com.erp.kernel.ddic.repository.SearchHelpRepository;
import com.erp.kernel.ddic.repository.TableDefinitionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for DDIC search help management.
 *
 * <p>Search helps define lookup mechanisms for table field values,
 * similar to SAP's search help functionality in transaction SE11.
 */
@Service
@Transactional(readOnly = true)
public class SearchHelpService {

    private final SearchHelpRepository searchHelpRepository;
    private final TableDefinitionRepository tableDefinitionRepository;

    /**
     * Creates a new search help service.
     *
     * @param searchHelpRepository      the search help repository
     * @param tableDefinitionRepository the table definition repository
     */
    public SearchHelpService(final SearchHelpRepository searchHelpRepository,
                             final TableDefinitionRepository tableDefinitionRepository) {
        this.searchHelpRepository = Objects.requireNonNull(searchHelpRepository,
                "searchHelpRepository must not be null");
        this.tableDefinitionRepository = Objects.requireNonNull(tableDefinitionRepository,
                "tableDefinitionRepository must not be null");
    }

    /**
     * Creates a new search help.
     *
     * @param request the creation request
     * @return the created search help DTO
     * @throws DuplicateEntityException if a search help with the same name exists
     * @throws EntityNotFoundException  if the referenced table definition is not found
     */
    @Transactional
    @CacheEvict(value = "searchHelps", allEntries = true)
    public SearchHelpDto create(final CreateSearchHelpRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (searchHelpRepository.existsBySearchHelpName(request.searchHelpName())) {
            throw new DuplicateEntityException(
                    "Search help with name '%s' already exists".formatted(request.searchHelpName()));
        }
        final var tableDefinition = tableDefinitionRepository.findById(request.tableDefinitionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Table definition with id %d not found"
                                .formatted(request.tableDefinitionId())));
        final var entity = SearchHelpMapper.toEntity(request, tableDefinition);
        final var saved = searchHelpRepository.save(entity);
        return SearchHelpMapper.toDto(saved);
    }

    /**
     * Finds a search help by its ID.
     *
     * @param id the search help ID
     * @return the search help DTO
     * @throws EntityNotFoundException if the search help is not found
     */
    @Cacheable(value = "searchHelps", key = "#id")
    public SearchHelpDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = searchHelpRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Search help with id %d not found".formatted(id)));
        return SearchHelpMapper.toDto(entity);
    }

    /**
     * Returns all search helps.
     *
     * @return the list of all search help DTOs
     */
    @Cacheable(value = "searchHelps", key = "'all'")
    public List<SearchHelpDto> findAll() {
        return searchHelpRepository.findAll().stream()
                .map(SearchHelpMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing search help.
     *
     * @param id      the search help ID
     * @param request the update request
     * @return the updated search help DTO
     * @throws EntityNotFoundException  if the search help or table definition is not found
     * @throws DuplicateEntityException if the new name conflicts with another search help
     */
    @Transactional
    @CacheEvict(value = "searchHelps", allEntries = true)
    public SearchHelpDto update(final Long id, final CreateSearchHelpRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = searchHelpRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Search help with id %d not found".formatted(id)));
        searchHelpRepository.findBySearchHelpName(request.searchHelpName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "Search help with name '%s' already exists"
                                    .formatted(request.searchHelpName()));
                });
        final var tableDefinition = tableDefinitionRepository.findById(request.tableDefinitionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Table definition with id %d not found"
                                .formatted(request.tableDefinitionId())));
        SearchHelpMapper.updateEntity(entity, request, tableDefinition);
        final var saved = searchHelpRepository.save(entity);
        return SearchHelpMapper.toDto(saved);
    }

    /**
     * Deletes a search help by its ID.
     *
     * @param id the search help ID
     * @throws EntityNotFoundException if the search help is not found
     */
    @Transactional
    @CacheEvict(value = "searchHelps", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!searchHelpRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Search help with id %d not found".formatted(id));
        }
        searchHelpRepository.deleteById(id);
    }
}
