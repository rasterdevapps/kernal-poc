package com.erp.kernel.benchmark;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the benchmarking subsystem.
 *
 * <p>Enables binding of {@link BenchmarkProperties} from application configuration.
 */
@Configuration
@EnableConfigurationProperties(BenchmarkProperties.class)
public class BenchmarkConfiguration {
}
