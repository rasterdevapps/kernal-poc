package com.erp.kernel.navigation.dto;

import java.time.Instant;

/**
 * Response DTO for theme information.
 *
 * @param id             the theme ID
 * @param themeName      the theme name
 * @param description    the description
 * @param primaryColor   the primary colour hex code
 * @param secondaryColor the secondary colour hex code
 * @param active         whether the theme is active
 * @param isDefault      whether this is the default theme
 * @param createdAt      the creation timestamp
 * @param updatedAt      the last update timestamp
 */
public record ThemeDto(
        Long id,
        String themeName,
        String description,
        String primaryColor,
        String secondaryColor,
        boolean active,
        boolean isDefault,
        Instant createdAt,
        Instant updatedAt
) {
}
