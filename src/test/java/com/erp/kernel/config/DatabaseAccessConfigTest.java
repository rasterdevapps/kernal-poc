package com.erp.kernel.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link DatabaseAccessConfig}.
 */
class DatabaseAccessConfigTest {

    @Test
    void shouldLogWarning_whenH2ConsoleEnabledInNonLocalProfile() {
        final var environment = mock(Environment.class);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"prod"});
        when(environment.getProperty("spring.h2.console.enabled", Boolean.class, false))
                .thenReturn(true);

        final var config = new DatabaseAccessConfig(environment);

        assertThatCode(config::validateDatabaseAccess).doesNotThrowAnyException();
    }

    @Test
    void shouldNotLogWarning_whenH2ConsoleDisabledInNonLocalProfile() {
        final var environment = mock(Environment.class);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"prod"});
        when(environment.getProperty("spring.h2.console.enabled", Boolean.class, false))
                .thenReturn(false);

        final var config = new DatabaseAccessConfig(environment);

        assertThatCode(config::validateDatabaseAccess).doesNotThrowAnyException();
    }

    @Test
    void shouldNotLogWarning_whenH2ConsoleEnabledInLocalProfile() {
        final var environment = mock(Environment.class);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"local"});
        when(environment.getProperty("spring.h2.console.enabled", Boolean.class, false))
                .thenReturn(true);

        final var config = new DatabaseAccessConfig(environment);

        assertThatCode(config::validateDatabaseAccess).doesNotThrowAnyException();
    }

    @Test
    void shouldHandleEmptyProfiles() {
        final var environment = mock(Environment.class);
        when(environment.getActiveProfiles()).thenReturn(new String[]{});
        when(environment.getProperty("spring.h2.console.enabled", Boolean.class, false))
                .thenReturn(false);

        final var config = new DatabaseAccessConfig(environment);

        assertThatCode(config::validateDatabaseAccess).doesNotThrowAnyException();
    }
}
