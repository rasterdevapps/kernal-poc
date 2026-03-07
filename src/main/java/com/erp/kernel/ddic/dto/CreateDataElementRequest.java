package com.erp.kernel.ddic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a DDIC data element.
 *
 * @param elementName the unique element name (required, max 30 chars)
 * @param domainId    the domain ID to reference (required)
 * @param shortLabel  the short label (max 10 chars)
 * @param mediumLabel the medium label (max 20 chars)
 * @param longLabel   the long label (max 40 chars)
 * @param description the description (max 255 chars)
 */
public record CreateDataElementRequest(
        @NotBlank @Size(max = 30) String elementName,
        @NotNull Long domainId,
        @Size(max = 10) String shortLabel,
        @Size(max = 20) String mediumLabel,
        @Size(max = 40) String longLabel,
        @Size(max = 255) String description
) {
}
