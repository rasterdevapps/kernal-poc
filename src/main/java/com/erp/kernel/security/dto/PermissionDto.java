package com.erp.kernel.security.dto;

import java.time.Instant;

/**
 * Response DTO for permission information.
 *
 * @param id             the permission ID
 * @param permissionName the permission name
 * @param description    the description
 * @param resource       the resource identifier
 * @param action         the action (e.g., READ, WRITE, DELETE)
 * @param createdAt      the creation timestamp
 * @param updatedAt      the last update timestamp
 */
public record PermissionDto(
        Long id,
        String permissionName,
        String description,
        String resource,
        String action,
        Instant createdAt,
        Instant updatedAt
) {
}
