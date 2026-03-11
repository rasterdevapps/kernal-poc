package com.erp.kernel.resilience.circuitbreaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * A thread-safe circuit breaker implementation providing fault isolation and automatic recovery.
 *
 * <p>Transitions through {@link CircuitBreakerState#CLOSED}, {@link CircuitBreakerState#OPEN},
 * and {@link CircuitBreakerState#HALF_OPEN} states based on configurable failure thresholds
 * and recovery timeouts.
 *
 * <p>State transitions:
 * <ul>
 *   <li>CLOSED &rarr; OPEN: when {@code failureThreshold} consecutive failures occur.</li>
 *   <li>OPEN &rarr; HALF_OPEN: after {@code openStateDurationMillis} have elapsed.</li>
 *   <li>HALF_OPEN &rarr; CLOSED: when a call succeeds.</li>
 *   <li>HALF_OPEN &rarr; OPEN: when a call fails.</li>
 * </ul>
 *
 * <p>Usage:
 * <pre>{@code
 * var result = circuitBreaker.execute(() -> externalService.call());
 * }</pre>
 */
public class CircuitBreaker {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitBreaker.class);

    private final String name;
    private final int failureThreshold;
    private final int halfOpenMaxCalls;
    private final long openStateDurationMillis;

    private final AtomicReference<CircuitBreakerState> state;
    private final AtomicInteger failureCount;
    private final AtomicInteger halfOpenCallCount;

    private volatile Instant openedAt;

    /**
     * Creates a new circuit breaker.
     *
     * @param name                    the circuit breaker name
     * @param failureThreshold        consecutive failures that trigger the OPEN state
     * @param halfOpenMaxCalls        total HALF_OPEN call attempts before rejecting further calls
     * @param openStateDurationMillis time in milliseconds to remain OPEN before probing recovery
     */
    public CircuitBreaker(final String name,
                          final int failureThreshold,
                          final int halfOpenMaxCalls,
                          final long openStateDurationMillis) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.failureThreshold = failureThreshold;
        this.halfOpenMaxCalls = halfOpenMaxCalls;
        this.openStateDurationMillis = openStateDurationMillis;
        this.state = new AtomicReference<>(CircuitBreakerState.CLOSED);
        this.failureCount = new AtomicInteger(0);
        this.halfOpenCallCount = new AtomicInteger(0);
    }

    /**
     * Executes the given supplier through the circuit breaker.
     *
     * @param <T>      the return type
     * @param supplier the operation to execute
     * @return the result of the supplier
     * @throws CircuitBreakerOpenException if the circuit is OPEN or HALF_OPEN limit is reached
     */
    public <T> T execute(final Supplier<T> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");
        checkAndTransition();

        final var currentState = state.get();

        if (currentState == CircuitBreakerState.OPEN) {
            throw new CircuitBreakerOpenException(
                    "Circuit breaker '" + name + "' is OPEN");
        }

        if (currentState == CircuitBreakerState.HALF_OPEN
                && halfOpenCallCount.get() >= halfOpenMaxCalls) {
            throw new CircuitBreakerOpenException(
                    "Circuit breaker '" + name + "' HALF_OPEN call limit reached");
        }

        if (currentState == CircuitBreakerState.HALF_OPEN) {
            halfOpenCallCount.incrementAndGet();
        }

        try {
            final T result = supplier.get();
            recordSuccess();
            return result;
        } catch (final Exception ex) {
            recordFailure();
            throw ex;
        }
    }

    /**
     * Returns the current state of this circuit breaker.
     *
     * @return the current {@link CircuitBreakerState}
     */
    public CircuitBreakerState getState() {
        checkAndTransition();
        return state.get();
    }

    /**
     * Returns the name of this circuit breaker.
     *
     * @return the circuit breaker name
     */
    public String getName() {
        return name;
    }

    /**
     * Resets the circuit breaker to {@link CircuitBreakerState#CLOSED} state.
     *
     * <p>Use this method to manually recover from a failure condition.
     */
    public void reset() {
        state.set(CircuitBreakerState.CLOSED);
        failureCount.set(0);
        halfOpenCallCount.set(0);
        openedAt = null;
        LOG.info("Circuit breaker '{}' reset to CLOSED", name);
    }

    private void checkAndTransition() {
        if (state.get() == CircuitBreakerState.OPEN) {
            final var elapsed = Instant.now().toEpochMilli() - openedAt.toEpochMilli();
            if (elapsed >= openStateDurationMillis) {
                transitionToHalfOpen();
            }
        }
    }

    private void recordSuccess() {
        if (state.get() == CircuitBreakerState.HALF_OPEN) {
            transitionToClosed();
        } else {
            failureCount.set(0);
        }
    }

    private void recordFailure() {
        if (state.get() == CircuitBreakerState.HALF_OPEN) {
            transitionToOpen();
        } else {
            final var failures = failureCount.incrementAndGet();
            if (failures >= failureThreshold) {
                transitionToOpen();
            }
        }
    }

    private void transitionToOpen() {
        state.set(CircuitBreakerState.OPEN);
        openedAt = Instant.now();
        LOG.warn("Circuit breaker '{}' transitioned to OPEN (failures={})",
                name, failureCount.get());
    }

    private void transitionToHalfOpen() {
        state.set(CircuitBreakerState.HALF_OPEN);
        LOG.info("Circuit breaker '{}' transitioned to HALF_OPEN", name);
    }

    private void transitionToClosed() {
        state.set(CircuitBreakerState.CLOSED);
        failureCount.set(0);
        halfOpenCallCount.set(0);
        openedAt = null;
        LOG.info("Circuit breaker '{}' transitioned to CLOSED", name);
    }
}
