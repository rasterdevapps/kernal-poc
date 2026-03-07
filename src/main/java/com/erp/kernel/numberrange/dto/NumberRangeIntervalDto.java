package com.erp.kernel.numberrange.dto;

import java.time.Instant;

/**
 * Response DTO representing a number range interval.
 *
 * @param id            the interval ID
 * @param numberRangeId the parent number range ID
 * @param subObject     the sub-object identifier
 * @param fromNumber    the lower bound
 * @param toNumber      the upper bound
 * @param currentNumber the current (last assigned) number
 * @param createdAt     the creation timestamp
 * @param updatedAt     the last update timestamp
 */
public record NumberRangeIntervalDto(
        Long id,
        Long numberRangeId,
        String subObject,
        String fromNumber,
        String toNumber,
        String currentNumber,
        Instant createdAt,
        Instant updatedAt
) {
}
