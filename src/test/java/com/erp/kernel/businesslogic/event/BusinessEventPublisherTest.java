package com.erp.kernel.businesslogic.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link BusinessEventPublisher}.
 */
class BusinessEventPublisherTest {

    private BusinessEventPublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new BusinessEventPublisher();
    }

    @Test
    void shouldPublishEvent_toRegisteredListener() {
        final var received = new ArrayList<BusinessEvent>();
        publisher.subscribe(received::add);

        final var event = BusinessEvent.of(BusinessEventType.CREATED, "Domain", 1L, null);
        publisher.publish(event);

        assertThat(received).hasSize(1);
        assertThat(received.getFirst().eventType()).isEqualTo(BusinessEventType.CREATED);
    }

    @Test
    void shouldPublishEvent_toMultipleListeners() {
        final var received1 = new ArrayList<BusinessEvent>();
        final var received2 = new ArrayList<BusinessEvent>();
        publisher.subscribe(received1::add);
        publisher.subscribe(received2::add);

        publisher.publish(BusinessEvent.of(BusinessEventType.UPDATED, "Domain", 1L, null));

        assertThat(received1).hasSize(1);
        assertThat(received2).hasSize(1);
    }

    @Test
    void shouldContinueDelivery_whenListenerThrows() {
        final var received = new ArrayList<BusinessEvent>();
        publisher.subscribe(event -> {
            throw new RuntimeException("Listener error");
        });
        publisher.subscribe(received::add);

        publisher.publish(BusinessEvent.of(BusinessEventType.DELETED, "Domain", 1L, null));

        assertThat(received).hasSize(1);
    }

    @Test
    void shouldReturnRegisteredListeners() {
        publisher.subscribe(event -> {});
        publisher.subscribe(event -> {});

        assertThat(publisher.getListeners()).hasSize(2);
    }

    @Test
    void shouldReturnUnmodifiableListenersList() {
        assertThatThrownBy(() -> publisher.getListeners().add(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldClearAllListeners() {
        publisher.subscribe(event -> {});
        publisher.clearListeners();

        assertThat(publisher.getListeners()).isEmpty();
    }

    @Test
    void shouldThrowNullPointerException_whenSubscribingNullListener() {
        assertThatThrownBy(() -> publisher.subscribe(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullPointerException_whenPublishingNullEvent() {
        assertThatThrownBy(() -> publisher.publish(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotFail_whenPublishingWithNoListeners() {
        publisher.publish(BusinessEvent.of(BusinessEventType.CREATED, "Domain", 1L, null));
        // No exception should be thrown
    }
}
