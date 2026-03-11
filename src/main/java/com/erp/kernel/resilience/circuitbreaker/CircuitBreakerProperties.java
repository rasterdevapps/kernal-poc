package com.erp.kernel.resilience.circuitbreaker;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for circuit breaker behaviour.
 *
 * @param failureThreshold         number of consecutive failures before the circuit opens
 * @param halfOpenMaxCalls         total calls permitted in HALF_OPEN state before rejecting
 * @param openStateDurationSeconds time in seconds the circuit remains OPEN before probing recovery
 */
@ConfigurationProperties(prefix = "erp.resilience.circuit-breaker")
public record CircuitBreakerProperties(
        int failureThreshold,
        int halfOpenMaxCalls,
        long openStateDurationSeconds
) {
}
