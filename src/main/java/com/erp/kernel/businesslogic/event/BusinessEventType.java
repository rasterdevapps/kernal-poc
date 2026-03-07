package com.erp.kernel.businesslogic.event;

/**
 * Enumerates the types of business events that can be published.
 */
public enum BusinessEventType {

    /** Entity creation event. */
    CREATED("An entity was created"),

    /** Entity update event. */
    UPDATED("An entity was updated"),

    /** Entity deletion event. */
    DELETED("An entity was deleted"),

    /** Validation event before persistence. */
    BEFORE_SAVE("Triggered before an entity is saved"),

    /** Post-processing event after persistence. */
    AFTER_SAVE("Triggered after an entity is saved");

    private final String description;

    BusinessEventType(final String description) {
        this.description = description;
    }

    /**
     * Returns a human-readable description of this event type.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
