package com.erp.kernel.datatypes.reference;

/**
 * Enumerates the categories of reference data types.
 *
 * <p>Reference types provide indirection to data and objects, enabling
 * polymorphic associations and late binding similar to ABAP reference types.
 */
public enum ReferenceCategory {

    /** Reference to a data object (similar to ABAP DATA REF). */
    DATA("Reference to a data object"),

    /** Reference to an object instance (similar to ABAP OBJECT REF). */
    OBJECT("Reference to an object instance"),

    /** Reference to an entity via type and identifier. */
    ENTITY("Reference to a persistent entity");

    private final String description;

    ReferenceCategory(final String description) {
        this.description = description;
    }

    /**
     * Returns a human-readable description of this reference category.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
