package com.erp.kernel.ddic.dto;

import java.time.Instant;

/**
 * Response DTO representing a DDIC data element.
 *
 * @param id          the data element ID
 * @param elementName the unique element name
 * @param domainId    the referenced domain ID
 * @param domainName  the referenced domain name
 * @param shortLabel  the short label
 * @param mediumLabel the medium label
 * @param longLabel   the long label
 * @param description the description
 * @param createdAt   the creation timestamp
 * @param updatedAt   the last update timestamp
 */
public record DataElementDto(
        Long id,
        String elementName,
        Long domainId,
        String domainName,
        String shortLabel,
        String mediumLabel,
        String longLabel,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}
