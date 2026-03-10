package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateRecentNavigationRequest;
import com.erp.kernel.navigation.dto.RecentNavigationDto;
import com.erp.kernel.navigation.mapper.RecentNavigationMapper;
import com.erp.kernel.navigation.repository.RecentNavigationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for recent navigation history.
 *
 * <p>Manages recording, retrieval, and deletion of recent navigation
 * entries that track which T-Codes a user has accessed, enabling a
 * "recently visited" list for quick re-navigation.
 */
@Service
@Transactional(readOnly = true)
public class RecentNavigationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecentNavigationService.class);

    private final RecentNavigationRepository recentNavigationRepository;

    /**
     * Creates a new recent navigation service.
     *
     * @param recentNavigationRepository the recent navigation repository
     */
    public RecentNavigationService(final RecentNavigationRepository recentNavigationRepository) {
        this.recentNavigationRepository = Objects.requireNonNull(recentNavigationRepository,
                "recentNavigationRepository must not be null");
    }

    /**
     * Records a new navigation entry with the current timestamp.
     *
     * @param request the creation request
     * @return the recorded navigation DTO
     */
    @Transactional
    @CacheEvict(value = "recentNavigation", allEntries = true)
    public RecentNavigationDto record(final CreateRecentNavigationRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        final var entity = RecentNavigationMapper.toEntity(request);
        final var saved = recentNavigationRepository.save(entity);
        LOG.info("Navigation recorded for user {} to T-Code {} with ID {}",
                saved.getUserId(), saved.getTcodeId(), saved.getId());
        return RecentNavigationMapper.toDto(saved);
    }

    /**
     * Finds a recent navigation entry by ID.
     *
     * @param id the navigation entry ID
     * @return the recent navigation DTO
     * @throws EntityNotFoundException if the entry is not found
     */
    @Cacheable(value = "recentNavigation", key = "#id")
    public RecentNavigationDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = recentNavigationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Recent navigation with id %d not found".formatted(id)));
        return RecentNavigationMapper.toDto(entity);
    }

    /**
     * Finds all recent navigation entries for a user, ordered by access time descending.
     *
     * @param userId the user ID
     * @return the list of recent navigation DTOs
     */
    @Cacheable(value = "recentNavigation", key = "'user:' + #userId")
    public List<RecentNavigationDto> findByUserId(final Long userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return recentNavigationRepository.findByUserIdOrderByAccessedAtDesc(userId).stream()
                .map(RecentNavigationMapper::toDto)
                .toList();
    }

    /**
     * Deletes all navigation history for a specific user.
     *
     * @param userId the user ID
     */
    @Transactional
    @CacheEvict(value = "recentNavigation", allEntries = true)
    public void deleteByUserId(final Long userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        recentNavigationRepository.deleteByUserId(userId);
        LOG.info("Navigation history deleted for user {}", userId);
    }
}
