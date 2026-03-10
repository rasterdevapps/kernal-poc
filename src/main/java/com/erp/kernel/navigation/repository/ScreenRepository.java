package com.erp.kernel.navigation.repository;

import com.erp.kernel.navigation.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Screen} entity persistence operations.
 */
public interface ScreenRepository extends JpaRepository<Screen, Long> {

    /**
     * Checks whether a screen exists with the given screen ID.
     *
     * @param screenId the screen identifier
     * @return {@code true} if a screen with the ID exists
     */
    boolean existsByScreenId(String screenId);

    /**
     * Finds a screen by screen ID.
     *
     * @param screenId the screen identifier
     * @return the screen if found
     */
    Optional<Screen> findByScreenId(String screenId);

    /**
     * Finds all screens belonging to a specific module.
     *
     * @param module the module name
     * @return the list of screens in the module
     */
    List<Screen> findByModule(String module);
}
