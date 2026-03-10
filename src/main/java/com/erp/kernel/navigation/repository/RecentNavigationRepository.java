package com.erp.kernel.navigation.repository;

import com.erp.kernel.navigation.entity.RecentNavigation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for {@link RecentNavigation} entity persistence operations.
 */
public interface RecentNavigationRepository extends JpaRepository<RecentNavigation, Long> {

    /**
     * Finds recent navigation entries for a user, ordered by access time descending.
     *
     * @param userId the user ID
     * @return the list of recent navigation entries
     */
    List<RecentNavigation> findByUserIdOrderByAccessedAtDesc(Long userId);

    /**
     * Deletes all navigation history for a specific user.
     *
     * @param userId the user ID
     */
    void deleteByUserId(Long userId);
}
