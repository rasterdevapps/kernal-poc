package com.erp.kernel.ddic.service;

import com.erp.kernel.ddic.dto.CreateDataElementRequest;
import com.erp.kernel.ddic.dto.DataElementDto;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.mapper.DataElementMapper;
import com.erp.kernel.ddic.repository.DataElementRepository;
import com.erp.kernel.ddic.repository.DomainRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for DDIC data element management.
 *
 * <p>Data elements define the semantic attributes (labels, descriptions) for table fields
 * and reference a domain for their technical properties.
 */
@Service
@Transactional(readOnly = true)
public class DataElementService {

    private final DataElementRepository dataElementRepository;
    private final DomainRepository domainRepository;

    /**
     * Creates a new data element service.
     *
     * @param dataElementRepository the data element repository
     * @param domainRepository      the domain repository
     */
    public DataElementService(final DataElementRepository dataElementRepository,
                              final DomainRepository domainRepository) {
        this.dataElementRepository = Objects.requireNonNull(dataElementRepository,
                "dataElementRepository must not be null");
        this.domainRepository = Objects.requireNonNull(domainRepository,
                "domainRepository must not be null");
    }

    /**
     * Creates a new data element.
     *
     * @param request the creation request
     * @return the created data element DTO
     * @throws DuplicateEntityException if a data element with the same name exists
     * @throws EntityNotFoundException  if the referenced domain is not found
     */
    @Transactional
    @CacheEvict(value = "dataElements", allEntries = true)
    public DataElementDto create(final CreateDataElementRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (dataElementRepository.existsByElementName(request.elementName())) {
            throw new DuplicateEntityException(
                    "Data element with name '%s' already exists".formatted(request.elementName()));
        }
        final var domain = domainRepository.findById(request.domainId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Domain with id %d not found".formatted(request.domainId())));
        final var entity = DataElementMapper.toEntity(request, domain);
        final var saved = dataElementRepository.save(entity);
        return DataElementMapper.toDto(saved);
    }

    /**
     * Finds a data element by its ID.
     *
     * @param id the data element ID
     * @return the data element DTO
     * @throws EntityNotFoundException if the data element is not found
     */
    @Cacheable(value = "dataElements", key = "#id")
    public DataElementDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = dataElementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Data element with id %d not found".formatted(id)));
        return DataElementMapper.toDto(entity);
    }

    /**
     * Returns all data elements.
     *
     * @return the list of all data element DTOs
     */
    @Cacheable(value = "dataElements", key = "'all'")
    public List<DataElementDto> findAll() {
        return dataElementRepository.findAll().stream()
                .map(DataElementMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing data element.
     *
     * @param id      the data element ID
     * @param request the update request
     * @return the updated data element DTO
     * @throws EntityNotFoundException  if the data element or referenced domain is not found
     * @throws DuplicateEntityException if the new name conflicts with another data element
     */
    @Transactional
    @CacheEvict(value = "dataElements", allEntries = true)
    public DataElementDto update(final Long id, final CreateDataElementRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = dataElementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Data element with id %d not found".formatted(id)));
        dataElementRepository.findByElementName(request.elementName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "Data element with name '%s' already exists".formatted(request.elementName()));
                });
        final var domain = domainRepository.findById(request.domainId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Domain with id %d not found".formatted(request.domainId())));
        DataElementMapper.updateEntity(entity, request, domain);
        final var saved = dataElementRepository.save(entity);
        return DataElementMapper.toDto(saved);
    }

    /**
     * Deletes a data element by its ID.
     *
     * @param id the data element ID
     * @throws EntityNotFoundException if the data element is not found
     */
    @Transactional
    @CacheEvict(value = "dataElements", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!dataElementRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Data element with id %d not found".formatted(id));
        }
        dataElementRepository.deleteById(id);
    }
}
