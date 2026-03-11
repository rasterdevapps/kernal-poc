package com.erp.kernel.resilience.circuitbreaker;

/**
 * Represents the state of a circuit breaker.
 *
 * <p>Circuit breakers transition through three states to provide fault isolation
 * and automatic recovery:
 * <ul>
 *   <li>{@link #CLOSED} — normal operation; all calls pass through.</li>
 *   <li>{@link #OPEN} — failure threshold exceeded; calls are rejected immediately.</li>
 *   <li>{@link #HALF_OPEN} — recovery probe mode; a limited number of calls are allowed.</li>
 * </ul>
 */
public enum CircuitBreakerState {

    /** Normal operation; all calls pass through. */
    CLOSED,

    /** Failure threshold exceeded; calls are rejected immediately. */
    OPEN,

    /** Recovery probe mode; a limited number of calls are permitted to test service health. */
    HALF_OPEN
}
