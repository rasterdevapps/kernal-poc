package com.erp.kernel.navigation.repository;

import com.erp.kernel.navigation.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for {@link Theme} entity persistence operations.
 */
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    /**
     * Checks whether a theme exists with the given name.
     *
     * @param themeName the theme name
     * @return {@code true} if a theme with the name exists
     */
    boolean existsByThemeName(String themeName);

    /**
     * Finds a theme by name.
     *
     * @param themeName the theme name
     * @return the theme if found
     */
    Optional<Theme> findByThemeName(String themeName);

    /**
     * Finds the default theme.
     *
     * @return the default theme if one is set
     */
    Optional<Theme> findByIsDefaultTrue();
}
