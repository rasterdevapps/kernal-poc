package com.erp.kernel.datatypes.reference;

import java.util.Objects;

/**
 * A typed reference to a data value, enabling indirect access to data objects.
 *
 * <p>A data reference holds both the target type name and the referenced value,
 * similar to ABAP's {@code REF TO data} concept.
 *
 * @param targetType the fully qualified type name of the referenced data
 * @param value      the referenced data value (may be {@code null})
 */
public record DataReference(String targetType, Object value) {

    /**
     * Creates a validated data reference.
     *
     * @param targetType the target type name (required)
     * @param value      the referenced value
     */
    public DataReference {
        Objects.requireNonNull(targetType, "targetType must not be null");
        if (targetType.isBlank()) {
            throw new IllegalArgumentException("targetType must not be blank");
        }
    }

    /**
     * Returns the reference category for data references.
     *
     * @return {@link ReferenceCategory#DATA}
     */
    public ReferenceCategory getCategory() {
        return ReferenceCategory.DATA;
    }

    /**
     * Checks whether this reference currently holds a value.
     *
     * @return {@code true} if the referenced value is not {@code null}
     */
    public boolean isAssigned() {
        return value != null;
    }
}
