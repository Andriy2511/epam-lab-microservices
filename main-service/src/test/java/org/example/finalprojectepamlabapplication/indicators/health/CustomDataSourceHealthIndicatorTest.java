package org.example.finalprojectepamlabapplication.indicators.health;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomDataSourceHealthIndicatorTest {

    private CustomDataSourceHealthIndicator healthIndicator;
    private HikariDataSource mockDataSource;
    private HikariPoolMXBean mockPoolMXBean;

    @BeforeEach
    void setUp() {
        mockDataSource = mock(HikariDataSource.class);
        mockPoolMXBean = mock(HikariPoolMXBean.class);

        when(mockDataSource.getHikariPoolMXBean()).thenReturn(mockPoolMXBean);
        when(mockPoolMXBean.getActiveConnections()).thenReturn(5);
        when(mockPoolMXBean.getIdleConnections()).thenReturn(3);
        when(mockPoolMXBean.getTotalConnections()).thenReturn(8);

        healthIndicator = new CustomDataSourceHealthIndicator(mockDataSource);
    }

    @Test
    void testDoHealthCheck() throws Exception {
        Health.Builder builder = new Health.Builder();

        Method addCustomDetailsMethod = CustomDataSourceHealthIndicator.class.getDeclaredMethod("addCustomDetails", Health.Builder.class);
        addCustomDetailsMethod.setAccessible(true);
        addCustomDetailsMethod.invoke(healthIndicator, builder);

        builder.up();
        Health health = builder.build();
        assertEquals(Status.UP, health.getStatus());

        assertEquals(5, health.getDetails().get("Active Connections"));
        assertEquals(3, health.getDetails().get("Idle Connections"));
        assertEquals(8, health.getDetails().get("Total Connections"));
    }
}
