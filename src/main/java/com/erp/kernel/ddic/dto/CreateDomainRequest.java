package com.erp.kernel.ddic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a DDIC domain.
 *
 * @param domainName    the unique domain name (required, max 30 chars)
 * @param dataType      the data type (required, max 20 chars)
 * @param maxLength     the maximum length
 * @param decimalPlaces the number of decimal places
 * @param description   the description (max 255 chars)
 */
public record CreateDomainRequest(
        @NotBlank @Size(max = 30) String domainName,
        @NotBlank @Size(max = 20) String dataType,
        Integer maxLength,
        Integer decimalPlaces,
        @Size(max = 255) String description
) {
}
