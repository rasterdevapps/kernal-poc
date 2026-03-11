package com.erp.kernel.resilience.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Custom Spring Boot health indicator that validates database connectivity.
 *
 * <p>Acquires a connection from the data source and reads the database product name
 * and version to confirm the database is reachable. Reports these as health details.
 */
@Component("databaseHealth")
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    /**
     * Creates a new database health indicator.
     *
     * @param dataSource the application data source
     */
    public DatabaseHealthIndicator(final DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource must not be null");
    }

    /**
     * Checks database health by acquiring and validating a connection.
     *
     * @return {@link Health#up()} with database details if reachable,
     *         {@link Health#down()} with error details otherwise
     */
    @Override
    public Health health() {
        try (var connection = dataSource.getConnection()) {
            final var metadata = connection.getMetaData();
            return Health.up()
                    .withDetail("database", metadata.getDatabaseProductName())
                    .withDetail("version", metadata.getDatabaseProductVersion())
                    .withDetail("valid", connection.isValid(2))
                    .build();
        } catch (final SQLException ex) {
            return Health.down()
                    .withDetail("error", ex.getMessage())
                    .build();
        }
    }
}
