package com.erp.kernel.resilience.circuitbreaker;

/**
 * Thrown when a call is rejected because the circuit breaker is OPEN.
 *
 * <p>Callers should catch this exception and apply graceful degradation logic,
 * such as returning a cached response or a fallback value.
 */
public class CircuitBreakerOpenException extends RuntimeException {

    /**
     * Creates a new exception with the specified message.
     *
     * @param message the detail message describing which circuit breaker is open
     */
    public CircuitBreakerOpenException(final String message) {
        super(message);
    }
}
