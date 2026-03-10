package com.erp.kernel.navigation.dto;

import java.time.Instant;

/**
 * Response DTO for user preference information.
 *
 * @param id           the preference ID
 * @param userId       the user ID
 * @param themeId      the preferred theme ID
 * @param locale       the locale code
 * @param dateFormat   the date format
 * @param timeFormat   the time format
 * @param itemsPerPage the items per page
 * @param createdAt    the creation timestamp
 * @param updatedAt    the last update timestamp
 */
public record UserPreferenceDto(
        Long id,
        Long userId,
        Long themeId,
        String locale,
        String dateFormat,
        String timeFormat,
        int itemsPerPage,
        Instant createdAt,
        Instant updatedAt
) {
}
