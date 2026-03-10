package com.erp.kernel.navigation.service;

import com.erp.kernel.ddic.exception.DuplicateEntityException;
import com.erp.kernel.ddic.exception.EntityNotFoundException;
import com.erp.kernel.navigation.dto.CreateFavouriteRequest;
import com.erp.kernel.navigation.dto.FavouriteDto;
import com.erp.kernel.navigation.mapper.FavouriteMapper;
import com.erp.kernel.navigation.repository.FavouriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service providing business logic for favourite management.
 *
 * <p>Manages user favourite T-Code operations including creation,
 * retrieval by user, and deletion. Favourites allow users to bookmark
 * frequently used transaction codes for quick access.
 */
@Service
@Transactional(readOnly = true)
public class FavouriteService {

    private static final Logger LOG = LoggerFactory.getLogger(FavouriteService.class);

    private final FavouriteRepository favouriteRepository;

    /**
     * Creates a new favourite service.
     *
     * @param favouriteRepository the favourite repository
     */
    public FavouriteService(final FavouriteRepository favouriteRepository) {
        this.favouriteRepository = Objects.requireNonNull(favouriteRepository,
                "favouriteRepository must not be null");
    }

    /**
     * Creates a new favourite.
     *
     * @param request the creation request
     * @return the created favourite DTO
     * @throws DuplicateEntityException if a favourite already exists for the user and T-Code
     */
    @Transactional
    @CacheEvict(value = "favourites", allEntries = true)
    public FavouriteDto create(final CreateFavouriteRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (favouriteRepository.existsByUserIdAndTcodeId(request.userId(), request.tcodeId())) {
            throw new DuplicateEntityException(
                    "Favourite already exists for user %d and T-code %d"
                            .formatted(request.userId(), request.tcodeId()));
        }
        final var entity = FavouriteMapper.toEntity(request);
        final var saved = favouriteRepository.save(entity);
        LOG.info("Favourite created for user {} and T-Code {} with ID {}",
                saved.getUserId(), saved.getTcodeId(), saved.getId());
        return FavouriteMapper.toDto(saved);
    }

    /**
     * Finds a favourite by ID.
     *
     * @param id the favourite ID
     * @return the favourite DTO
     * @throws EntityNotFoundException if the favourite is not found
     */
    @Cacheable(value = "favourites", key = "#id")
    public FavouriteDto findById(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        final var entity = favouriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Favourite with id %d not found".formatted(id)));
        return FavouriteMapper.toDto(entity);
    }

    /**
     * Finds all favourites for a specific user, ordered by sort order.
     *
     * @param userId the user ID
     * @return the list of favourite DTOs ordered by sort order
     */
    @Cacheable(value = "favourites", key = "'user:' + #userId")
    public List<FavouriteDto> findByUserId(final Long userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return favouriteRepository.findByUserIdOrderBySortOrderAsc(userId).stream()
                .map(FavouriteMapper::toDto)
                .toList();
    }

    /**
     * Deletes a favourite by ID.
     *
     * @param id the favourite ID
     * @throws EntityNotFoundException if the favourite is not found
     */
    @Transactional
    @CacheEvict(value = "favourites", allEntries = true)
    public void delete(final Long id) {
        Objects.requireNonNull(id, "id must not be null");
        if (!favouriteRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Favourite with id %d not found".formatted(id));
        }
        favouriteRepository.deleteById(id);
        LOG.info("Favourite with ID {} deleted", id);
    }
}
