package com.erp.kernel.benchmark;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BenchmarkConfiguration}.
 */
class BenchmarkConfigurationTest {

    @Test
    void shouldInstantiateConfiguration() {
        final var configuration = new BenchmarkConfiguration();

        assertThat(configuration).isNotNull();
    }
}
