package com.erp.kernel.businesslogic.event;

/**
 * Interface for listening to {@link BusinessEvent} instances.
 *
 * <p>Implementations register with the {@link BusinessEventPublisher}
 * to receive notifications when business events occur.
 */
@FunctionalInterface
public interface BusinessEventListener {

    /**
     * Handles a business event.
     *
     * @param event the business event to handle
     */
    void onEvent(BusinessEvent event);
}
