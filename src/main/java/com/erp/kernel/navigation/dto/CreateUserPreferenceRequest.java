package com.erp.kernel.navigation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating user preferences.
 *
 * @param userId      the user ID
 * @param themeId     the preferred theme ID (optional)
 * @param locale      the locale code (e.g., en, de, fr)
 * @param dateFormat  the preferred date format
 * @param timeFormat  the preferred time format
 * @param itemsPerPage the number of items per page
 */
public record CreateUserPreferenceRequest(
        @NotNull Long userId,
        Long themeId,
        @NotBlank @Size(max = 10) String locale,
        @NotBlank @Size(max = 20) String dateFormat,
        @NotBlank @Size(max = 20) String timeFormat,
        @Min(1) int itemsPerPage
) {
}
