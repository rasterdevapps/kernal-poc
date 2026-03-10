package com.erp.kernel.navigation.repository;

import com.erp.kernel.navigation.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for {@link UserPreference} entity persistence operations.
 */
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    /**
     * Finds the preferences for a specific user.
     *
     * @param userId the user ID
     * @return the user preferences if found
     */
    Optional<UserPreference> findByUserId(Long userId);

    /**
     * Checks whether preferences exist for a specific user.
     *
     * @param userId the user ID
     * @return {@code true} if preferences exist for the user
     */
    boolean existsByUserId(Long userId);
}
