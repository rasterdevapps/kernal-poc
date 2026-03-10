package com.erp.kernel.navigation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating a favourite T-code entry.
 *
 * @param userId    the user ID
 * @param tcodeId   the T-code ID
 * @param sortOrder the sort order for display
 */
public record CreateFavouriteRequest(
        @NotNull Long userId,
        @NotNull Long tcodeId,
        @Min(0) int sortOrder
) {
}
