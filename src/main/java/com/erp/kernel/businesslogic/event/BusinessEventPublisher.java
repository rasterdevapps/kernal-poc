package com.erp.kernel.businesslogic.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Publishes {@link BusinessEvent} instances to registered {@link BusinessEventListener} subscribers.
 *
 * <p>Events are delivered synchronously in the order listeners were registered.
 * Listener failures are logged but do not prevent delivery to subsequent listeners.
 */
@Component
public class BusinessEventPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessEventPublisher.class);

    private final List<BusinessEventListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Registers a listener to receive business events.
     *
     * @param listener the listener to register
     */
    public void subscribe(final BusinessEventListener listener) {
        Objects.requireNonNull(listener, "listener must not be null");
        listeners.add(listener);
    }

    /**
     * Publishes a business event to all registered listeners.
     *
     * @param event the event to publish
     */
    public void publish(final BusinessEvent event) {
        Objects.requireNonNull(event, "event must not be null");
        LOG.debug("Publishing event: {} for entity {} (id={})",
                event.eventType(), event.entityType(), event.entityId());
        for (final var listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (final RuntimeException ex) {
                LOG.error("Listener failed for event {}: {}",
                        event.eventType(), ex.getMessage(), ex);
            }
        }
    }

    /**
     * Returns the registered listeners as an unmodifiable list.
     *
     * @return the listeners
     */
    public List<BusinessEventListener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    /**
     * Removes all registered listeners. Primarily for testing.
     */
    public void clearListeners() {
        listeners.clear();
    }
}
