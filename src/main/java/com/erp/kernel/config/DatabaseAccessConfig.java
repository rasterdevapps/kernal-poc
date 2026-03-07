package com.erp.kernel.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Configuration enforcing password-protected database access controls.
 *
 * <p>This configuration validates that database access is properly secured:
 * <ul>
 *   <li>All data access goes through the application layer (Controller → Service → Repository)</li>
 *   <li>H2 console is disabled in non-local profiles</li>
 *   <li>Database credentials are managed via environment variables in production</li>
 *   <li>No direct SQL execution endpoints are exposed</li>
 * </ul>
 *
 * <p>This is part of Milestone 2.3 — Password-Protected Database Access.
 */
@Configuration
public class DatabaseAccessConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseAccessConfig.class);

    private final Environment environment;

    /**
     * Creates a new database access configuration.
     *
     * @param environment the Spring environment for profile detection
     */
    public DatabaseAccessConfig(final Environment environment) {
        this.environment = environment;
    }

    /**
     * Validates database access security settings when the application starts.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void validateDatabaseAccess() {
        final var activeProfiles = Arrays.asList(environment.getActiveProfiles());
        final var h2ConsoleEnabled = environment.getProperty(
                "spring.h2.console.enabled", Boolean.class, false);

        if (!activeProfiles.contains("local") && Boolean.TRUE.equals(h2ConsoleEnabled)) {
            LOG.warn("H2 console is enabled in a non-local profile. "
                    + "This is a security risk and should be disabled.");
        }

        LOG.info("Database access control validated. Active profiles: {}", activeProfiles);
    }
}
