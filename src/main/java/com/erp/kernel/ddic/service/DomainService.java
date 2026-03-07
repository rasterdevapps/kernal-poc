package com.erp.kernel.ddic.service;

import com.erp.kernel.ddic.dto.CreateDomainRequest;
import com.erp.kernel.ddic.dto.DomainDto;
import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.ddic.mapper.DomainMapper;
import com.erp.kernel.ddic.repository.DomainRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for DDIC domain management.
 *
 * <p>Domains define the technical attributes (data type, length, decimal places)
 * used by data elements. Results are cached for improved read performance.
 */
@Service
@Transactional(readOnly = true)
public class DomainService {

    private final DomainRepository domainRepository;

    /**
     * Creates a new domain service.
     *
     * @param domainRepository the domain repository
     */
    public DomainService(final DomainRepository domainRepository) {
        this.domainRepository = Objects.requireNonNull(domainRepository, "domainRepository must not be null");
    }

    /**
     * Creates a new domain.
     *
     * @param request the creation request
     * @return the created domain DTO
     * @throws DuplicateEntityException if a domain with the same name exists
     */
    @Transactional
    @CacheEvict(value = "domains", allEntries = true)
    public DomainDto create(final CreateDomainRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (domainRepository.existsByDomainName(request.domainName())) {
            throw new DuplicateEntityException(
                    "Domain with name '%s' already exists".formatted(request.domainName()));
        }
        final var entity = DomainMapper.toEntity(request);
        final var saved = domainRepository.save(entity);
        return DomainMapper.toDto(saved);
    }

    /**
     * Finds a domain by its ID.
     *
     * @param id the domain ID
     * @return the domain DTO
     * @throws EntityNotFoundException if the domain is not found
     */
    @Cacheable(value = "domains", key = "#id")
    public DomainDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = domainRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Domain with id %d not found".formatted(id)));
        return DomainMapper.toDto(entity);
    }

    /**
     * Returns all domains.
     *
     * @return the list of all domain DTOs
     */
    @Cacheable(value = "domains", key = "'all'")
    public List<DomainDto> findAll() {
        return domainRepository.findAll().stream()
                .map(DomainMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing domain.
     *
     * @param id      the domain ID
     * @param request the update request
     * @return the updated domain DTO
     * @throws EntityNotFoundException  if the domain is not found
     * @throws DuplicateEntityException if the new name conflicts with another domain
     */
    @Transactional
    @CacheEvict(value = "domains", allEntries = true)
    public DomainDto update(final Long id, final CreateDomainRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = domainRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Domain with id %d not found".formatted(id)));
        domainRepository.findByDomainName(request.domainName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "Domain with name '%s' already exists".formatted(request.domainName()));
                });
        DomainMapper.updateEntity(entity, request);
        final var saved = domainRepository.save(entity);
        return DomainMapper.toDto(saved);
    }

    /**
     * Deletes a domain by its ID.
     *
     * @param id the domain ID
     * @throws EntityNotFoundException if the domain is not found
     */
    @Transactional
    @CacheEvict(value = "domains", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!domainRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Domain with id %d not found".formatted(id));
        }
        domainRepository.deleteById(id);
    }
}
