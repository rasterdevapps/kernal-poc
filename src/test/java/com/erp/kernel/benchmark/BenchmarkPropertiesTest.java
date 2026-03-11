package com.erp.kernel.benchmark;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BenchmarkProperties}.
 */
class BenchmarkPropertiesTest {

    @Test
    void shouldCreateProperties_whenAllFieldsProvided() {
        final var properties = new BenchmarkProperties(true, 3, 10, 50);

        assertThat(properties.enabled()).isTrue();
        assertThat(properties.warmupIterations()).isEqualTo(3);
        assertThat(properties.measureIterations()).isEqualTo(10);
        assertThat(properties.concurrentUsers()).isEqualTo(50);
    }

    @Test
    void shouldCreateProperties_whenDisabled() {
        final var properties = new BenchmarkProperties(false, 0, 0, 0);

        assertThat(properties.enabled()).isFalse();
        assertThat(properties.warmupIterations()).isZero();
        assertThat(properties.measureIterations()).isZero();
        assertThat(properties.concurrentUsers()).isZero();
    }

    @Test
    void shouldImplementEquality() {
        final var properties1 = new BenchmarkProperties(true, 5, 20, 100);
        final var properties2 = new BenchmarkProperties(true, 5, 20, 100);

        assertThat(properties1).isEqualTo(properties2);
        assertThat(properties1.hashCode()).isEqualTo(properties2.hashCode());
    }

    @Test
    void shouldNotBeEqual_whenFieldsDiffer() {
        final var properties1 = new BenchmarkProperties(true, 5, 20, 100);
        final var properties2 = new BenchmarkProperties(false, 5, 20, 100);

        assertThat(properties1).isNotEqualTo(properties2);
    }

    @Test
    void shouldReturnReadableToString() {
        final var properties = new BenchmarkProperties(true, 3, 10, 50);

        assertThat(properties.toString()).contains("true");
        assertThat(properties.toString()).contains("3");
        assertThat(properties.toString()).contains("10");
        assertThat(properties.toString()).contains("50");
    }
}
