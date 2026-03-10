package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateUserPreferenceRequest;
import com.erp.kernel.navigation.dto.UserPreferenceDto;
import com.erp.kernel.navigation.mapper.UserPreferenceMapper;
import com.erp.kernel.navigation.repository.UserPreferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Service providing business logic for user preference management.
 *
 * <p>Manages per-user presentation preferences including theme selection,
 * locale, date/time format, and pagination settings. Each user has at
 * most one preference record.
 */
@Service
@Transactional(readOnly = true)
public class UserPreferenceService {

    private static final Logger LOG = LoggerFactory.getLogger(UserPreferenceService.class);

    private final UserPreferenceRepository userPreferenceRepository;

    /**
     * Creates a new user preference service.
     *
     * @param userPreferenceRepository the user preference repository
     */
    public UserPreferenceService(final UserPreferenceRepository userPreferenceRepository) {
        this.userPreferenceRepository = Objects.requireNonNull(userPreferenceRepository,
                "userPreferenceRepository must not be null");
    }

    /**
     * Creates new user preferences.
     *
     * @param request the creation request
     * @return the created user preference DTO
     * @throws DuplicateEntityException if preferences already exist for the user
     */
    @Transactional
    @CacheEvict(value = "userPreferences", allEntries = true)
    public UserPreferenceDto create(final CreateUserPreferenceRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (userPreferenceRepository.existsByUserId(request.userId())) {
            throw new DuplicateEntityException(
                    "Preferences already exist for user %d".formatted(request.userId()));
        }
        final var entity = UserPreferenceMapper.toEntity(request);
        final var saved = userPreferenceRepository.save(entity);
        LOG.info("Preferences created for user {} with ID {}", saved.getUserId(), saved.getId());
        return UserPreferenceMapper.toDto(saved);
    }

    /**
     * Finds user preferences by ID.
     *
     * @param id the preference ID
     * @return the user preference DTO
     * @throws EntityNotFoundException if the preferences are not found
     */
    @Cacheable(value = "userPreferences", key = "#id")
    public UserPreferenceDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = userPreferenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User preference with id %d not found".formatted(id)));
        return UserPreferenceMapper.toDto(entity);
    }

    /**
     * Finds user preferences by user ID.
     *
     * @param userId the user ID
     * @return the user preference DTO
     * @throws EntityNotFoundException if no preferences exist for the user
     */
    @Cacheable(value = "userPreferences", key = "'user:' + #userId")
    public UserPreferenceDto findByUserId(final Long userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        final var entity = userPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Preferences for user %d not found".formatted(userId)));
        return UserPreferenceMapper.toDto(entity);
    }

    /**
     * Updates existing user preferences.
     *
     * @param id      the preference ID
     * @param request the update request
     * @return the updated user preference DTO
     * @throws EntityNotFoundException if the preferences are not found
     */
    @Transactional
    @CacheEvict(value = "userPreferences", allEntries = true)
    public UserPreferenceDto update(final Long id, final CreateUserPreferenceRequest request) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(request, "request must not be null");
        final var entity = userPreferenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User preference with id %d not found".formatted(id)));
        UserPreferenceMapper.updateEntity(entity, request);
        final var saved = userPreferenceRepository.save(entity);
        LOG.info("Preferences updated for user {}", saved.getUserId());
        return UserPreferenceMapper.toDto(saved);
    }

    /**
     * Deletes user preferences by ID.
     *
     * @param id the preference ID
     * @throws EntityNotFoundException if the preferences are not found
     */
    @Transactional
    @CacheEvict(value = "userPreferences", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!userPreferenceRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "User preference with id %d not found".formatted(id));
        }
        userPreferenceRepository.deleteById(id);
        LOG.info("Preferences with ID {} deleted", id);
    }
}
