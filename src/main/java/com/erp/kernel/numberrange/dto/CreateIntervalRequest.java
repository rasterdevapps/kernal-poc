package com.erp.kernel.numberrange.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a number range interval.
 *
 * @param subObject  the sub-object identifier (required, max 2 chars)
 * @param fromNumber the lower bound of the interval (required, max 20 chars)
 * @param toNumber   the upper bound of the interval (required, max 20 chars)
 */
public record CreateIntervalRequest(
        @NotBlank @Size(max = 2) String subObject,
        @NotBlank @Size(max = 20) String fromNumber,
        @NotBlank @Size(max = 20) String toNumber
) {
}
