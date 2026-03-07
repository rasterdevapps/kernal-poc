package com.erp.kernel.numberrange.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a number range.
 *
 * @param rangeObject the range object name (required, max 30 chars)
 * @param description the description (max 255 chars)
 * @param buffered    whether the range uses buffered assignment
 * @param bufferSize  the buffer size for buffered ranges
 */
public record CreateNumberRangeRequest(
        @NotBlank @Size(max = 30) String rangeObject,
        @Size(max = 255) String description,
        boolean buffered,
        Integer bufferSize
) {
}
