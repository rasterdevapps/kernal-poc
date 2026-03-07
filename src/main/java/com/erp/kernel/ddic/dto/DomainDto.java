package com.erp.kernel.ddic.dto;

import java.time.Instant;

/**
 * Response DTO representing a DDIC domain.
 *
 * @param id            the domain ID
 * @param domainName    the unique domain name
 * @param dataType      the data type
 * @param maxLength     the maximum length
 * @param decimalPlaces the number of decimal places
 * @param description   the description
 * @param createdAt     the creation timestamp
 * @param updatedAt     the last update timestamp
 */
public record DomainDto(
        Long id,
        String domainName,
        String dataType,
        Integer maxLength,
        Integer decimalPlaces,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}
