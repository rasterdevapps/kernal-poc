package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateScreenRequest;
import com.erp.kernel.navigation.dto.ScreenDto;
import com.erp.kernel.navigation.mapper.ScreenMapper;
import com.erp.kernel.navigation.repository.ScreenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for screen management.
 *
 * <p>Manages screen CRUD operations including creation, retrieval,
 * update, deletion, and queries by module for the presentation layer
 * screen definitions.
 */
@Service
@Transactional(readOnly = true)
public class ScreenService {

    private static final Logger LOG = LoggerFactory.getLogger(ScreenService.class);

    private final ScreenRepository screenRepository;

    /**
     * Creates a new screen service.
     *
     * @param screenRepository the screen repository
     */
    public ScreenService(final ScreenRepository screenRepository) {
        this.screenRepository = Objects.requireNonNull(screenRepository, "screenRepository must not be null");
    }

    /**
     * Creates a new screen.
     *
     * @param request the creation request
     * @return the created screen DTO
     * @throws DuplicateEntityException if a screen with the same screen ID exists
     */
    @Transactional
    @CacheEvict(value = "screens", allEntries = true)
    public ScreenDto create(final CreateScreenRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (screenRepository.existsByScreenId(request.screenId())) {
            throw new DuplicateEntityException(
                    "Screen with screen ID '%s' already exists".formatted(request.screenId()));
        }
        final var entity = ScreenMapper.toEntity(request);
        final var saved = screenRepository.save(entity);
        LOG.info("Screen '{}' created with ID {}", saved.getScreenId(), saved.getId());
        return ScreenMapper.toDto(saved);
    }

    /**
     * Finds a screen by ID.
     *
     * @param id the screen ID
     * @return the screen DTO
     * @throws EntityNotFoundException if the screen is not found
     */
    @Cacheable(value = "screens", key = "#id")
    public ScreenDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = screenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Screen with id %d not found".formatted(id)));
        return ScreenMapper.toDto(entity);
    }

    /**
     * Returns all screens.
     *
     * @return the list of all screen DTOs
     */
    @Cacheable(value = "screens", key = "'all'")
    public List<ScreenDto> findAll() {
        return screenRepository.findAll().stream()
                .map(ScreenMapper::toDto)
                .toList();
    }

    /**
     * Finds all screens belonging to a specific module.
     *
     * @param module the module name
     * @return the list of screen DTOs in the module
     */
    @Cacheable(value = "screens", key = "'module:' + #module")
    public List<ScreenDto> findByModule(final String module) {
        Objects.requireNonNull(module, "module must not be null");
        return screenRepository.findByModule(module).stream()
                .map(ScreenMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing screen.
     *
     * @param id      the screen ID
     * @param request the update request
     * @return the updated screen DTO
     * @throws EntityNotFoundException  if the screen is not found
     * @throws DuplicateEntityException if the new screen ID conflicts with another screen
     */
    @Transactional
    @CacheEvict(value = "screens", allEntries = true)
    public ScreenDto update(final Long id, final CreateScreenRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = screenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Screen with id %d not found".formatted(id)));
        screenRepository.findByScreenId(request.screenId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "Screen with screen ID '%s' already exists".formatted(request.screenId()));
                });
        ScreenMapper.updateEntity(entity, request);
        final var saved = screenRepository.save(entity);
        LOG.info("Screen '{}' updated", saved.getScreenId());
        return ScreenMapper.toDto(saved);
    }

    /**
     * Deletes a screen by ID.
     *
     * @param id the screen ID
     * @throws EntityNotFoundException if the screen is not found
     */
    @Transactional
    @CacheEvict(value = "screens", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!screenRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Screen with id %d not found".formatted(id));
        }
        screenRepository.deleteById(id);
        LOG.info("Screen with ID {} deleted", id);
    }
}
