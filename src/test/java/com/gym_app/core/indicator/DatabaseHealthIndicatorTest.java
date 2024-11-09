package com.gym_app.core.indicator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.health.Health;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DatabaseHealthIndicatorTest {

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private DatabaseHealthIndicator databaseHealthIndicator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDatabaseHealthUp() throws Exception {
        // Mocking a valid connection
        Connection mockConnection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isValid(1000)).thenReturn(true);

        // Invoke the health() method directly
        Health health = databaseHealthIndicator.health();

        // Verify the health status
        assertEquals(Health.up().withDetail("Database", "Available").build(), health);
    }

    @Test
    public void testDatabaseHealthDown() throws Exception {
        // Mocking an invalid connection
        Connection mockConnection = Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isValid(1000)).thenReturn(false);

        // Invoke the health() method directly
        Health health = databaseHealthIndicator.health();

        // Verify the health status
        assertEquals(Health.down().withDetail("Database", "Unavailable").build(), health);
    }

    @Test
    public void testDatabaseHealthError() throws Exception {
        // Mocking SQLException
        SQLException sqlException = new SQLException("Connection error");
        when(dataSource.getConnection()).thenThrow(sqlException);

        // Invoke the health() method directly
        Health health = databaseHealthIndicator.health();

        // Verify the health status including the error field
        Health expectedHealth = Health.down(sqlException)
                .withDetail("Database", "Error")
                .build();
        assertEquals(expectedHealth, health);
    }
}
