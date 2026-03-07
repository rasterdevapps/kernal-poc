package com.erp.kernel.datatypes.reference;

import java.util.Objects;

/**
 * A typed reference to an object instance, enabling polymorphic associations.
 *
 * <p>An object reference holds the target class name and the referenced object,
 * similar to ABAP's {@code REF TO object} concept.
 *
 * @param targetClassName the fully qualified class name of the referenced object
 * @param instance        the referenced object instance (may be {@code null})
 */
public record ObjectReference(String targetClassName, Object instance) {

    /**
     * Creates a validated object reference.
     *
     * @param targetClassName the target class name (required)
     * @param instance        the referenced object instance
     */
    public ObjectReference {
        Objects.requireNonNull(targetClassName, "targetClassName must not be null");
        if (targetClassName.isBlank()) {
            throw new IllegalArgumentException("targetClassName must not be blank");
        }
    }

    /**
     * Returns the reference category for object references.
     *
     * @return {@link ReferenceCategory#OBJECT}
     */
    public ReferenceCategory getCategory() {
        return ReferenceCategory.OBJECT;
    }

    /**
     * Checks whether this reference currently points to an object instance.
     *
     * @return {@code true} if the instance is not {@code null}
     */
    public boolean isAssigned() {
        return instance != null;
    }
}
