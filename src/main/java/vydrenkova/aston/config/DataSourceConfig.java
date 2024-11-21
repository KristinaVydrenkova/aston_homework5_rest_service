package vydrenkova.aston.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class for the DataSource of the application.
 * Uses HikariCP for managing the database connection pool.
 * The configuration is loaded from the application.properties file.
 */
public class DataSourceConfig {

    /**
     * The single instance of the DataSource created based on the configuration.
     */
    private static final HikariDataSource dataSource;

    static {
        try (InputStream input = DataSourceConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password"));
            config.setDriverClassName(properties.getProperty("db.driverClassName"));

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DataSource", e);
        }
    }

    /**
     * Returns the DataSource instance.
     *
     * @return the DataSource instance configured based on the properties.
     */
    public static DataSource getDataSource() {
        return dataSource;
    }
}