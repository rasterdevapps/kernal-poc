package com.erp.kernel.businesslogic.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a business event occurring within the application.
 *
 * <p>Business events carry the event type, the entity type involved,
 * an optional payload, and the timestamp of occurrence.
 *
 * @param eventType  the type of business event
 * @param entityType the entity type involved (e.g., "Domain", "NumberRange")
 * @param entityId   the identifier of the affected entity (may be {@code null})
 * @param payload    the event payload data (may be {@code null})
 * @param timestamp  the instant when the event occurred
 */
public record BusinessEvent(
        BusinessEventType eventType,
        String entityType,
        Long entityId,
        Object payload,
        Instant timestamp
) {

    /**
     * Creates a validated business event.
     *
     * @param eventType  the event type (required)
     * @param entityType the entity type (required)
     * @param entityId   the entity identifier
     * @param payload    the event payload
     * @param timestamp  the event timestamp (required)
     */
    public BusinessEvent {
        Objects.requireNonNull(eventType, "eventType must not be null");
        Objects.requireNonNull(entityType, "entityType must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");
    }

    /**
     * Creates a business event with the current timestamp.
     *
     * @param eventType  the event type
     * @param entityType the entity type
     * @param entityId   the entity identifier
     * @param payload    the event payload
     * @return a new business event
     */
    public static BusinessEvent of(final BusinessEventType eventType,
                                   final String entityType,
                                   final Long entityId,
                                   final Object payload) {
        return new BusinessEvent(eventType, entityType, entityId, payload, Instant.now());
    }
}
