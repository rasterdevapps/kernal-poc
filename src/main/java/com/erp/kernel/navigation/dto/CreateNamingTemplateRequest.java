package com.erp.kernel.navigation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a naming template.
 *
 * @param entityType  the entity type (e.g., SCREEN, COMPONENT, API, TCODE)
 * @param pattern     the naming pattern
 * @param description the description
 * @param example     an example of the naming pattern applied
 */
public record CreateNamingTemplateRequest(
        @NotBlank @Size(max = 50) String entityType,
        @NotBlank @Size(max = 255) String pattern,
        @Size(max = 500) String description,
        @Size(max = 255) String example
) {
}
