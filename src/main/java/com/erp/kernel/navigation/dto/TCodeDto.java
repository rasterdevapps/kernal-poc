package com.erp.kernel.navigation.dto;

import java.time.Instant;

/**
 * Response DTO for T-code information.
 *
 * @param id          the T-code ID
 * @param code        the T-code
 * @param description the description
 * @param module      the module
 * @param route       the navigation route
 * @param icon        the icon identifier
 * @param active      whether the T-code is active
 * @param createdAt   the creation timestamp
 * @param updatedAt   the last update timestamp
 */
public record TCodeDto(
        Long id,
        String code,
        String description,
        String module,
        String route,
        String icon,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
