package com.erp.kernel.datatypes.complex;

import com.erp.kernel.datatypes.elementary.ElementaryDataType;

import java.util.Objects;

/**
 * Represents a single field within a {@link StructureDefinition}.
 *
 * <p>Each field has a name, an elementary data type, and optional
 * length and decimal constraints that govern value storage and validation.
 *
 * @param fieldName     the field name
 * @param dataType      the elementary data type of this field
 * @param maxLength     the maximum length (may be {@code null})
 * @param decimalPlaces the decimal places (may be {@code null})
 * @param description   the field description (may be {@code null})
 */
public record StructureField(
        String fieldName,
        ElementaryDataType dataType,
        Integer maxLength,
        Integer decimalPlaces,
        String description
) {

    /**
     * Creates a validated structure field.
     *
     * @param fieldName     the field name (required)
     * @param dataType      the elementary data type (required)
     * @param maxLength     the maximum length
     * @param decimalPlaces the decimal places
     * @param description   the description
     */
    public StructureField {
        Objects.requireNonNull(fieldName, "fieldName must not be null");
        Objects.requireNonNull(dataType, "dataType must not be null");
        if (fieldName.isBlank()) {
            throw new IllegalArgumentException("fieldName must not be blank");
        }
    }
}
