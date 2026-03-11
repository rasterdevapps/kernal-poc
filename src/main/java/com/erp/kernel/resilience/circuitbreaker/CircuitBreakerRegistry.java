package com.erp.kernel.resilience.circuitbreaker;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Registry for managing named circuit breaker instances.
 *
 * <p>Provides thread-safe creation and retrieval of {@link CircuitBreaker} instances
 * using a shared configuration. Each circuit breaker is identified by a unique name.
 */
public class CircuitBreakerRegistry {

    private final CircuitBreakerProperties properties;
    private final ConcurrentMap<String, CircuitBreaker> circuitBreakers;

    /**
     * Creates a new circuit breaker registry.
     *
     * @param properties the shared circuit breaker configuration
     */
    public CircuitBreakerRegistry(final CircuitBreakerProperties properties) {
        this.properties = Objects.requireNonNull(properties, "properties must not be null");
        this.circuitBreakers = new ConcurrentHashMap<>();
    }

    /**
     * Returns the circuit breaker with the given name, creating it if it does not yet exist.
     *
     * @param name the circuit breaker name
     * @return the existing or newly created circuit breaker
     */
    public CircuitBreaker getOrCreate(final String name) {
        Objects.requireNonNull(name, "name must not be null");
        return circuitBreakers.computeIfAbsent(name, n -> new CircuitBreaker(
                n,
                properties.failureThreshold(),
                properties.halfOpenMaxCalls(),
                properties.openStateDurationSeconds() * 1000L));
    }

    /**
     * Returns all registered circuit breaker instances.
     *
     * @return an unmodifiable view of all circuit breakers
     */
    public Collection<CircuitBreaker> getAll() {
        return Collections.unmodifiableCollection(circuitBreakers.values());
    }
}
