package com.erp.kernel.navigation.dto;

import java.time.Instant;

/**
 * Response DTO for screen definition information.
 *
 * @param id          the screen definition ID
 * @param screenId    the unique screen identifier
 * @param title       the screen title
 * @param description the description
 * @param module      the module
 * @param tcodeId     the associated T-code ID
 * @param screenType  the screen type
 * @param createdAt   the creation timestamp
 * @param updatedAt   the last update timestamp
 */
public record ScreenDto(
        Long id,
        String screenId,
        String title,
        String description,
        String module,
        Long tcodeId,
        String screenType,
        Instant createdAt,
        Instant updatedAt
) {
}
