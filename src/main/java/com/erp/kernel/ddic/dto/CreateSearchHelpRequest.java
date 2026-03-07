package com.erp.kernel.ddic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a DDIC search help.
 *
 * @param searchHelpName    the unique search help name (required, max 30 chars)
 * @param tableDefinitionId the table definition ID (required)
 * @param description       the description (max 255 chars)
 */
public record CreateSearchHelpRequest(
        @NotBlank @Size(max = 30) String searchHelpName,
        @NotNull Long tableDefinitionId,
        @Size(max = 255) String description
) {
}
