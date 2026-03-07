package com.erp.kernel.numberrange.dto;

/**
 * Response DTO containing the next assigned number from a number range.
 *
 * @param rangeObject   the range object name
 * @param subObject     the sub-object identifier
 * @param assignedNumber the newly assigned number
 */
public record NextNumberResponse(
        String rangeObject,
        String subObject,
        String assignedNumber
) {
}
