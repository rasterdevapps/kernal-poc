package com.erp.kernel.navigation.repository;

import com.erp.kernel.navigation.entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Favourite} entity persistence operations.
 */
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    /**
     * Finds all favourites for a specific user, ordered by sort order.
     *
     * @param userId the user ID
     * @return the list of favourites ordered by sort order
     */
    List<Favourite> findByUserIdOrderBySortOrderAsc(Long userId);

    /**
     * Checks whether a favourite exists for the given user and T-code.
     *
     * @param userId  the user ID
     * @param tcodeId the T-code ID
     * @return {@code true} if the favourite exists
     */
    boolean existsByUserIdAndTcodeId(Long userId, Long tcodeId);

    /**
     * Finds a favourite by user ID and T-code ID.
     *
     * @param userId  the user ID
     * @param tcodeId the T-code ID
     * @return the favourite if found
     */
    Optional<Favourite> findByUserIdAndTcodeId(Long userId, Long tcodeId);

    /**
     * Deletes all favourites for a specific user.
     *
     * @param userId the user ID
     */
    void deleteByUserId(Long userId);
}
