package com.erp.kernel.navigation.dto;

import java.time.Instant;

/**
 * Response DTO for recent navigation history information.
 *
 * @param id         the history entry ID
 * @param userId     the user ID
 * @param tcodeId    the T-code ID
 * @param accessedAt the access timestamp
 * @param createdAt  the creation timestamp
 * @param updatedAt  the last update timestamp
 */
public record RecentNavigationDto(
        Long id,
        Long userId,
        Long tcodeId,
        Instant accessedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
