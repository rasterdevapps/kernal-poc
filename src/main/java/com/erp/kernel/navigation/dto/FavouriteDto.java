package com.erp.kernel.navigation.dto;

import java.time.Instant;

/**
 * Response DTO for favourite T-code information.
 *
 * @param id        the favourite ID
 * @param userId    the user ID
 * @param tcodeId   the T-code ID
 * @param sortOrder the sort order
 * @param createdAt the creation timestamp
 * @param updatedAt the last update timestamp
 */
public record FavouriteDto(
        Long id,
        Long userId,
        Long tcodeId,
        int sortOrder,
        Instant createdAt,
        Instant updatedAt
) {
}
