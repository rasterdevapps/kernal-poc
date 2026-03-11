package com.erp.kernel.resilience.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link DatabaseHealthIndicator}.
 */
@ExtendWith(MockitoExtension.class)
class DatabaseHealthIndicatorTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private DatabaseMetaData metadata;

    private DatabaseHealthIndicator indicator;

    @BeforeEach
    void setUp() {
        indicator = new DatabaseHealthIndicator(dataSource);
    }

    @Test
    void shouldThrowNullPointerException_whenDataSourceIsNull() {
        assertThatThrownBy(() -> new DatabaseHealthIndicator(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("dataSource must not be null");
    }

    @Test
    void shouldReturnUp_whenDatabaseIsReachable() throws SQLException {
        // Arrange
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metadata);
        when(metadata.getDatabaseProductName()).thenReturn("H2");
        when(metadata.getDatabaseProductVersion()).thenReturn("2.3.230");
        when(connection.isValid(2)).thenReturn(true);

        // Act
        final var health = indicator.health();

        // Assert
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsKey("database");
        assertThat(health.getDetails()).containsKey("version");
        assertThat(health.getDetails()).containsKey("valid");
    }

    @Test
    void shouldReturnDown_whenDatabaseIsUnreachable() throws SQLException {
        // Arrange
        when(dataSource.getConnection()).thenThrow(new SQLException("Connection refused"));

        // Act
        final var health = indicator.health();

        // Assert
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsKey("error");
    }
}
