package com.erp.kernel.navigation.dto;

import java.time.Instant;

/**
 * Response DTO for naming template information.
 *
 * @param id          the template ID
 * @param entityType  the entity type
 * @param pattern     the naming pattern
 * @param description the description
 * @param example     the pattern example
 * @param createdAt   the creation timestamp
 * @param updatedAt   the last update timestamp
 */
public record NamingTemplateDto(
        Long id,
        String entityType,
        String pattern,
        String description,
        String example,
        Instant createdAt,
        Instant updatedAt
) {
}
