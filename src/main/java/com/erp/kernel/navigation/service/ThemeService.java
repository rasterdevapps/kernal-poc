package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateThemeRequest;
import com.erp.kernel.navigation.dto.ThemeDto;
import com.erp.kernel.navigation.mapper.ThemeMapper;
import com.erp.kernel.navigation.repository.ThemeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for theme management.
 *
 * <p>Manages theme CRUD operations including creation, retrieval,
 * update, and deletion of UI themes used in the presentation layer.
 */
@Service
@Transactional(readOnly = true)
public class ThemeService {

    private static final Logger LOG = LoggerFactory.getLogger(ThemeService.class);

    private final ThemeRepository themeRepository;

    /**
     * Creates a new theme service.
     *
     * @param themeRepository the theme repository
     */
    public ThemeService(final ThemeRepository themeRepository) {
        this.themeRepository = Objects.requireNonNull(themeRepository, "themeRepository must not be null");
    }

    /**
     * Creates a new theme.
     *
     * @param request the creation request
     * @return the created theme DTO
     * @throws DuplicateEntityException if a theme with the same name exists
     */
    @Transactional
    @CacheEvict(value = "themes", allEntries = true)
    public ThemeDto create(final CreateThemeRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (themeRepository.existsByThemeName(request.themeName())) {
            throw new DuplicateEntityException(
                    "Theme with name '%s' already exists".formatted(request.themeName()));
        }
        final var entity = ThemeMapper.toEntity(request);
        final var saved = themeRepository.save(entity);
        LOG.info("Theme '{}' created with ID {}", saved.getThemeName(), saved.getId());
        return ThemeMapper.toDto(saved);
    }

    /**
     * Finds a theme by ID.
     *
     * @param id the theme ID
     * @return the theme DTO
     * @throws EntityNotFoundException if the theme is not found
     */
    @Cacheable(value = "themes", key = "#id")
    public ThemeDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = themeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Theme with id %d not found".formatted(id)));
        return ThemeMapper.toDto(entity);
    }

    /**
     * Returns all themes.
     *
     * @return the list of all theme DTOs
     */
    @Cacheable(value = "themes", key = "'all'")
    public List<ThemeDto> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeMapper::toDto)
                .toList();
    }

    /**
     * Updates an existing theme.
     *
     * @param id      the theme ID
     * @param request the update request
     * @return the updated theme DTO
     * @throws EntityNotFoundException  if the theme is not found
     * @throws DuplicateEntityException if the new name conflicts with another theme
     */
    @Transactional
    @CacheEvict(value = "themes", allEntries = true)
    public ThemeDto update(final Long id, final CreateThemeRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = themeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Theme with id %d not found".formatted(id)));
        themeRepository.findByThemeName(request.themeName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateEntityException(
                            "Theme with name '%s' already exists".formatted(request.themeName()));
                });
        ThemeMapper.updateEntity(entity, request);
        final var saved = themeRepository.save(entity);
        LOG.info("Theme '{}' updated", saved.getThemeName());
        return ThemeMapper.toDto(saved);
    }

    /**
     * Deletes a theme by ID.
     *
     * @param id the theme ID
     * @throws EntityNotFoundException if the theme is not found
     */
    @Transactional
    @CacheEvict(value = "themes", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!themeRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Theme with id %d not found".formatted(id));
        }
        themeRepository.deleteById(id);
        LOG.info("Theme with ID {} deleted", id);
    }
}
