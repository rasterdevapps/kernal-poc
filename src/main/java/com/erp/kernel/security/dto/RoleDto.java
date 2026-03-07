package com.erp.kernel.security.dto;

import java.time.Instant;

/**
 * Response DTO for role information.
 *
 * @param id          the role ID
 * @param roleName    the role name
 * @param description the role description
 * @param active      whether the role is active
 * @param createdAt   the creation timestamp
 * @param updatedAt   the last update timestamp
 */
public record RoleDto(
        Long id,
        String roleName,
        String description,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
