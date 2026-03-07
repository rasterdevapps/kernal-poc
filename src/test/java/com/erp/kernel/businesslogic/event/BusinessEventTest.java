package com.erp.kernel.businesslogic.event;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link BusinessEvent}.
 */
class BusinessEventTest {

    @Test
    void shouldCreateEvent_withAllFields() {
        final var now = Instant.now();
        final var event = new BusinessEvent(
                BusinessEventType.CREATED, "Domain", 1L, "payload", now);

        assertThat(event.eventType()).isEqualTo(BusinessEventType.CREATED);
        assertThat(event.entityType()).isEqualTo("Domain");
        assertThat(event.entityId()).isEqualTo(1L);
        assertThat(event.payload()).isEqualTo("payload");
        assertThat(event.timestamp()).isEqualTo(now);
    }

    @Test
    void shouldCreateEvent_withNullEntityIdAndPayload() {
        final var event = new BusinessEvent(
                BusinessEventType.DELETED, "Domain", null, null, Instant.now());

        assertThat(event.entityId()).isNull();
        assertThat(event.payload()).isNull();
    }

    @Test
    void shouldCreateEvent_usingFactoryMethod() {
        final var event = BusinessEvent.of(BusinessEventType.UPDATED, "Domain", 1L, "data");

        assertThat(event.eventType()).isEqualTo(BusinessEventType.UPDATED);
        assertThat(event.entityType()).isEqualTo("Domain");
        assertThat(event.entityId()).isEqualTo(1L);
        assertThat(event.payload()).isEqualTo("data");
        assertThat(event.timestamp()).isNotNull();
    }

    @Test
    void shouldThrowNullPointerException_whenEventTypeIsNull() {
        assertThatThrownBy(() -> new BusinessEvent(null, "Domain", 1L, null, Instant.now()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("eventType");
    }

    @Test
    void shouldThrowNullPointerException_whenEntityTypeIsNull() {
        assertThatThrownBy(() -> new BusinessEvent(
                BusinessEventType.CREATED, null, 1L, null, Instant.now()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("entityType");
    }

    @Test
    void shouldThrowNullPointerException_whenTimestampIsNull() {
        assertThatThrownBy(() -> new BusinessEvent(
                BusinessEventType.CREATED, "Domain", 1L, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("timestamp");
    }
}
