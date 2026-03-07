package com.erp.kernel.datatypes.reference;

import java.util.Objects;

/**
 * A reference to a persistent entity identified by its type and unique identifier.
 *
 * <p>Entity references enable loose coupling between entities by storing only
 * the entity type and ID rather than a direct object reference.
 *
 * @param entityType the entity type name (e.g., table or class name)
 * @param entityId   the entity identifier
 */
public record EntityReference(String entityType, Long entityId) {

    /**
     * Creates a validated entity reference.
     *
     * @param entityType the entity type name (required)
     * @param entityId   the entity identifier (required)
     */
    public EntityReference {
        Objects.requireNonNull(entityType, "entityType must not be null");
        Objects.requireNonNull(entityId, "entityId must not be null");
        if (entityType.isBlank()) {
            throw new IllegalArgumentException("entityType must not be blank");
        }
    }

    /**
     * Returns the reference category for entity references.
     *
     * @return {@link ReferenceCategory#ENTITY}
     */
    public ReferenceCategory getCategory() {
        return ReferenceCategory.ENTITY;
    }
}
