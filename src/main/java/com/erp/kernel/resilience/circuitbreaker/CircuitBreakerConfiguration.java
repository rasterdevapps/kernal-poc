package com.erp.kernel.resilience.circuitbreaker;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * Spring configuration class for circuit breaker components.
 *
 * <p>Registers the {@link CircuitBreakerRegistry} bean and enables
 * {@link CircuitBreakerProperties} configuration binding.
 */
@Configuration
@EnableConfigurationProperties(CircuitBreakerProperties.class)
public class CircuitBreakerConfiguration {

    /**
     * Creates the circuit breaker registry bean.
     *
     * @param properties the circuit breaker configuration properties
     * @return the circuit breaker registry
     */
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(
            final CircuitBreakerProperties properties) {
        Objects.requireNonNull(properties, "properties must not be null");
        return new CircuitBreakerRegistry(properties);
    }
}
