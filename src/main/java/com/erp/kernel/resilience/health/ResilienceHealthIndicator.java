package com.erp.kernel.resilience.health;

import com.erp.kernel.resilience.circuitbreaker.CircuitBreakerRegistry;
import com.erp.kernel.resilience.circuitbreaker.CircuitBreakerState;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Custom Spring Boot health indicator for circuit breakers.
 *
 * <p>Reports the current state of all registered circuit breakers.
 * The overall health status is {@code DOWN} if any circuit breaker is {@code OPEN}.
 */
@Component("resilienceHealth")
public class ResilienceHealthIndicator implements HealthIndicator {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    /**
     * Creates a new resilience health indicator.
     *
     * @param circuitBreakerRegistry the circuit breaker registry
     */
    public ResilienceHealthIndicator(final CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = Objects.requireNonNull(
                circuitBreakerRegistry, "circuitBreakerRegistry must not be null");
    }

    /**
     * Checks circuit breaker health by inspecting all registered circuit breakers.
     *
     * @return {@link Health#up()} if no circuit breakers are OPEN,
     *         {@link Health#down()} if any circuit breaker is OPEN
     */
    @Override
    public Health health() {
        final var circuitBreakers = circuitBreakerRegistry.getAll();
        final var details = new LinkedHashMap<String, Object>();
        var anyOpen = false;

        for (final var cb : circuitBreakers) {
            final var currentState = cb.getState();
            details.put(cb.getName(), currentState.name());
            if (currentState == CircuitBreakerState.OPEN) {
                anyOpen = true;
            }
        }

        details.put("total", circuitBreakers.size());

        if (anyOpen) {
            return Health.down().withDetails(details).build();
        }
        return Health.up().withDetails(details).build();
    }
}
