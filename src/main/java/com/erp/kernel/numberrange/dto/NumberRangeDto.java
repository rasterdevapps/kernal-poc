package com.erp.kernel.numberrange.dto;

import java.time.Instant;

/**
 * Response DTO representing a number range object.
 *
 * @param id          the number range ID
 * @param rangeObject the range object name
 * @param description the description
 * @param buffered    whether the range uses buffered assignment
 * @param bufferSize  the buffer size for buffered ranges
 * @param createdAt   the creation timestamp
 * @param updatedAt   the last update timestamp
 */
public record NumberRangeDto(
        Long id,
        String rangeObject,
        String description,
        boolean buffered,
        Integer bufferSize,
        Instant createdAt,
        Instant updatedAt
) {
}
