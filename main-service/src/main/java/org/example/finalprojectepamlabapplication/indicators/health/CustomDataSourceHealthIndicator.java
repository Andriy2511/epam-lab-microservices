package org.example.finalprojectepamlabapplication.indicators.health;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

@Component
public class CustomDataSourceHealthIndicator extends DataSourceHealthIndicator {

    private final DataSource dataSource;

    public CustomDataSourceHealthIndicator(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        super.doHealthCheck(builder);
        addCustomDetails(builder);
    }

    private void addCustomDetails(Health.Builder builder) {
        if (dataSource instanceof com.zaxxer.hikari.HikariDataSource) {
            com.zaxxer.hikari.HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            int activeConnections = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
            int idleConnections = hikariDataSource.getHikariPoolMXBean().getIdleConnections();
            int totalConnections = hikariDataSource.getHikariPoolMXBean().getTotalConnections();

            builder.withDetail("Active Connections", activeConnections)
                    .withDetail("Idle Connections", idleConnections)
                    .withDetail("Total Connections", totalConnections);
        }
    }
}