package com.erp.kernel.benchmark;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link BenchmarkService}.
 */
class BenchmarkServiceTest {

    private BenchmarkService service;

    @BeforeEach
    void setUp() {
        final var properties = new BenchmarkProperties(true, 3, 10, 50);
        service = new BenchmarkService(properties);
    }

    @Test
    void shouldThrowNullPointerException_whenPropertiesIsNull() {
        assertThatThrownBy(() -> new BenchmarkService(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("properties must not be null");
    }

    @Test
    void shouldRegisterScenario() {
        final var scenario = createScenario("test-scenario", "A test scenario");

        service.registerScenario(scenario);

        assertThat(service.getScenarios()).hasSize(1);
        assertThat(service.getScenarios().getFirst().getName()).isEqualTo("test-scenario");
    }

    @Test
    void shouldThrowNullPointerException_whenRegisteringNullScenario() {
        assertThatThrownBy(() -> service.registerScenario(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("scenario must not be null");
    }

    @Test
    void shouldExecuteScenario_whenEnabledAndFound() {
        final var scenario = createScenario("perf-test", "Performance test");
        service.registerScenario(scenario);

        final var result = service.executeScenario("perf-test");

        assertThat(result).isPresent();
        assertThat(result.get().scenarioName()).isEqualTo("perf-test");
        assertThat(service.getResults()).hasSize(1);
    }

    @Test
    void shouldReturnEmpty_whenBenchmarkingDisabled() {
        final var disabledProperties = new BenchmarkProperties(false, 3, 10, 50);
        final var disabledService = new BenchmarkService(disabledProperties);
        final var scenario = createScenario("disabled-test", "Should not run");
        disabledService.registerScenario(scenario);

        final var result = disabledService.executeScenario("disabled-test");

        assertThat(result).isEmpty();
        assertThat(disabledService.getResults()).isEmpty();
    }

    @Test
    void shouldReturnEmpty_whenScenarioNotFound() {
        final var result = service.executeScenario("nonexistent");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrowNullPointerException_whenExecutingNullScenarioName() {
        assertThatThrownBy(() -> service.executeScenario(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("scenarioName must not be null");
    }

    @Test
    void shouldReturnUnmodifiableScenariosList() {
        service.registerScenario(createScenario("s1", "Scenario 1"));

        final var scenarios = service.getScenarios();

        assertThatThrownBy(() -> scenarios.add(createScenario("s2", "Scenario 2")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldReturnUnmodifiableResultsList() {
        service.registerScenario(createScenario("s1", "Scenario 1"));
        service.executeScenario("s1");

        final var results = service.getResults();

        assertThatThrownBy(() -> results.add(BenchmarkResult.of("x", 0L, Map.of())))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldReturnProperties() {
        final var properties = service.getProperties();

        assertThat(properties.enabled()).isTrue();
        assertThat(properties.warmupIterations()).isEqualTo(3);
        assertThat(properties.measureIterations()).isEqualTo(10);
        assertThat(properties.concurrentUsers()).isEqualTo(50);
    }

    @Test
    void shouldClearScenariosAndResults() {
        service.registerScenario(createScenario("s1", "Scenario 1"));
        service.executeScenario("s1");

        assertThat(service.getScenarios()).hasSize(1);
        assertThat(service.getResults()).hasSize(1);

        service.clear();

        assertThat(service.getScenarios()).isEmpty();
        assertThat(service.getResults()).isEmpty();
    }

    @Test
    void shouldReturnEmptyScenariosListInitially() {
        assertThat(service.getScenarios()).isEmpty();
    }

    @Test
    void shouldReturnEmptyResultsListInitially() {
        assertThat(service.getResults()).isEmpty();
    }

    @Test
    void shouldExecuteCorrectScenario_whenMultipleScenariosRegistered() {
        service.registerScenario(createScenario("first", "First scenario"));
        service.registerScenario(createScenario("second", "Second scenario"));
        service.registerScenario(createScenario("third", "Third scenario"));

        final var result = service.executeScenario("second");

        assertThat(result).isPresent();
        assertThat(result.get().scenarioName()).isEqualTo("second");
    }

    private BenchmarkScenario createScenario(final String name, final String description) {
        return new BenchmarkScenario() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public BenchmarkResult execute() {
                return new BenchmarkResult(
                        name,
                        Instant.now(),
                        100L,
                        Map.of(BenchmarkMetric.THROUGHPUT, 1000.0)
                );
            }
        };
    }
}
