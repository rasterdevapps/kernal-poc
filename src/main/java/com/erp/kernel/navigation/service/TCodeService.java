package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateTCodeRequest;
import com.erp.kernel.navigation.dto.TCodeDto;
import com.erp.kernel.navigation.mapper.TCodeMapper;
import com.erp.kernel.navigation.repository.TCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for transaction code management.
 *
 * <p>Manages T-Code CRUD operations including creation, retrieval,
 * update, deletion, and queries by code, module, and active status.
 */
@Service
@Transactional(readOnly = true)
public class TCodeService {

    private static final Logger LOG = LoggerFactory.getLogger(TCodeService.class);

    private final TCodeRepository tCodeRepository;

    /**
     * Creates a new T-Code service.
     *
     * @param tCodeRepository the T-Code repository
     */
    public TCodeService(final TCodeRepository tCodeRepository) {
        this.tCodeRepository = Objects.requireNonNull(tCodeRepository, "tCodeRepository must not be null");
    }

    /**
     * Creates a new transaction code.
     *
     * @param request the creation request
     * @return the created T-Code DTO
     * @throws DuplicateEntityException if a T-Code with the same code exists
     */
    @Transactional
    @CacheEvict(value = "tcodes", allEntries = true)
    public TCodeDto create(final CreateTCodeRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (tCodeRepository.existsByCode(request.code())) {
            throw new DuplicateEntityException(
                    "T-Code with code '%s' already exists".formatted(request.code()));
        }
        final var entity = TCodeMapper.toEntity(request);
        final var saved = tCodeRepository.save(entity);
        LOG.info("T-Code '{}' created with ID {}", saved.getCode(), saved.getId());
        return TCodeMapper.toDto(saved);
    }

    /**
     * Finds a transaction code by ID.
     *
     * @param id the T-Code ID
     * @return the T-Code DTO
     * @throws EntityNotFoundException if the T-Code is not found
     */
    @Cacheable(value = "tcodes", key = "#id")
    public TCodeDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = tCodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "T-Code with id %d not found".formatted(id)));
        return TCodeMapper.toDto(entity);
    }

    /**
     * Returns all transaction codes.
     *
     * @return the list of all T-Code DTOs
     */
    @Cacheable(value = "tcodes", key = "'all'")
    public List<TCodeDto> findAll() {
        return tCodeRepository.findAll().stream()
                .map(TCodeMapper::toDto)
                .toList();
    }

    /**
     * Finds a transaction code by its unique code.
     *
     * @param code the T-Code code
     * @return the T-Code DTO
     * @throws EntityNotFoundException if the T-Code is not found
     */
    @Cacheable(value = "tcodes", key = "'code:' + #code")
    public TCodeDto findByCode(final String code) {
        Objects.requireNonNull(code, "code must not be null");
        final var entity = tCodeRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException(
                        "T-Code with code '%s' not found".formatted(code)));
        return TCodeMapper.toDto(entity);
    }

    /**
     * Finds all transaction codes belonging to a specific module.
     *
     * @param module the module name
     * @return the list of T-Code DTOs in the module
     */
    @Cacheable(value = "tcodes", key = "'module:' + #module")
    public List<TCodeDto> findByModule(final String module) {
        Objects.requireNonNull(module, "module must not be null");
        return tCodeRepository.findByModule(module).stream()
                .map(TCodeMapper::toDto)
                .toList();
    }

    /**
     * Finds all active transaction codes.
     *
     * @return the list of active T-Code DTOs
     */
    @Cacheable(value = "tcodes", key = "'active'")
    public List<TCodeDto> findAllActive() {
        return tCodeRepository.findByActiveTrue().stream()
                .map(TCodeMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing transaction code.
     *
     * @param id      the T-Code ID
     * @param request the update request
     * @return the updated T-Code DTO
     * @throws EntityNotFoundException  if the T-Code is not found
     * @throws DuplicateEntityException if the new code conflicts with another T-Code
     */
    @Transactional
    @CacheEvict(value = "tcodes", allEntries = true)
    public TCodeDto update(final Long id, final CreateTCodeRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = tCodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "T-Code with id %d not found".formatted(id)));
        tCodeRepository.findByCode(request.code())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "T-Code with code '%s' already exists".formatted(request.code()));
                });
        TCodeMapper.updateEntity(entity, request);
        final var saved = tCodeRepository.save(entity);
        LOG.info("T-Code '{}' updated", saved.getCode());
        return TCodeMapper.toDto(saved);
    }

    /**
     * Deletes a transaction code by ID.
     *
     * @param id the T-Code ID
     * @throws EntityNotFoundException if the T-Code is not found
     */
    @Transactional
    @CacheEvict(value = "tcodes", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!tCodeRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "T-Code with id %d not found".formatted(id));
        }
        tCodeRepository.deleteById(id);
        LOG.info("T-Code with ID {} deleted", id);
    }
}
