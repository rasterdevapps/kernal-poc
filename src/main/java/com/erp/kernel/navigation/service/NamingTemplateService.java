package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateNamingTemplateRequest;
import com.erp.kernel.navigation.dto.NamingTemplateDto;
import com.erp.kernel.navigation.mapper.NamingTemplateMapper;
import com.erp.kernel.navigation.repository.NamingTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for naming template management.
 *
 * <p>Manages naming template CRUD operations including creation,
 * retrieval, update, and deletion of templates that define standardised
 * entity identifier patterns.
 */
@Service
@Transactional(readOnly = true)
public class NamingTemplateService {

    private static final Logger LOG = LoggerFactory.getLogger(NamingTemplateService.class);

    private final NamingTemplateRepository namingTemplateRepository;

    /**
     * Creates a new naming template service.
     *
     * @param namingTemplateRepository the naming template repository
     */
    public NamingTemplateService(final NamingTemplateRepository namingTemplateRepository) {
        this.namingTemplateRepository = Objects.requireNonNull(namingTemplateRepository,
                "namingTemplateRepository must not be null");
    }

    /**
     * Creates a new naming template.
     *
     * @param request the creation request
     * @return the created naming template DTO
     * @throws DuplicateEntityException if a template for the same entity type exists
     */
    @Transactional
    @CacheEvict(value = "namingTemplates", allEntries = true)
    public NamingTemplateDto create(final CreateNamingTemplateRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (namingTemplateRepository.existsByEntityType(request.entityType())) {
            throw new DuplicateEntityException(
                    "Naming template for entity type '%s' already exists".formatted(request.entityType()));
        }
        final var entity = NamingTemplateMapper.toEntity(request);
        final var saved = namingTemplateRepository.save(entity);
        LOG.info("Naming template for '{}' created with ID {}", saved.getEntityType(), saved.getId());
        return NamingTemplateMapper.toDto(saved);
    }

    /**
     * Finds a naming template by ID.
     *
     * @param id the naming template ID
     * @return the naming template DTO
     * @throws EntityNotFoundException if the naming template is not found
     */
    @Cacheable(value = "namingTemplates", key = "#id")
    public NamingTemplateDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = namingTemplateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Naming template with id %d not found".formatted(id)));
        return NamingTemplateMapper.toDto(entity);
    }

    /**
     * Returns all naming templates.
     *
     * @return the list of all naming template DTOs
     */
    @Cacheable(value = "namingTemplates", key = "'all'")
    public List<NamingTemplateDto> findAll() {
        return namingTemplateRepository.findAll().stream()
                .map(NamingTemplateMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing naming template.
     *
     * @param id      the naming template ID
     * @param request the update request
     * @return the updated naming template DTO
     * @throws EntityNotFoundException  if the naming template is not found
     * @throws DuplicateEntityException if the new entity type conflicts with another template
     */
    @Transactional
    @CacheEvict(value = "namingTemplates", allEntries = true)
    public NamingTemplateDto update(final Long id, final CreateNamingTemplateRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = namingTemplateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Naming template with id %d not found".formatted(id)));
        namingTemplateRepository.findByEntityType(request.entityType())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "Naming template for entity type '%s' already exists"
                                    .formatted(request.entityType()));
                });
        NamingTemplateMapper.updateEntity(entity, request);
        final var saved = namingTemplateRepository.save(entity);
        LOG.info("Naming template for '{}' updated", saved.getEntityType());
        return NamingTemplateMapper.toDto(saved);
    }

    /**
     * Deletes a naming template by ID.
     *
     * @param id the naming template ID
     * @throws EntityNotFoundException if the naming template is not found
     */
    @Transactional
    @CacheEvict(value = "namingTemplates", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!namingTemplateRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Naming template with id %d not found".formatted(id));
        }
        namingTemplateRepository.deleteById(id);
        LOG.info("Naming template with ID {} deleted", id);
    }
}
