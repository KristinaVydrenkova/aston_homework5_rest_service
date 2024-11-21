package vydrenkova.aston.containers;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresTestContainer implements BeforeAllCallback, AfterAllCallback {

    private static PostgreSQLContainer<?> postgresContainer;
    private static DataSource dataSource;

    @Override
    public void beforeAll(ExtensionContext context) {
        postgresContainer = new PostgreSQLContainer<>("postgres:14")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpassword");

        postgresContainer.start();

        dataSource = new BasicDataSource();
        ((BasicDataSource) dataSource).setUrl(postgresContainer.getJdbcUrl());
        ((BasicDataSource) dataSource).setUsername(postgresContainer.getUsername());
        ((BasicDataSource) dataSource).setPassword(postgresContainer.getPassword());

        createTables();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        postgresContainer.stop();
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    private static void createTables() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE books (" +
                    "id SERIAL PRIMARY KEY," +
                    "title VARCHAR(255) NOT NULL," +
                    "author VARCHAR(255) NOT NULL," +
                    "genre VARCHAR(255) NOT NULL," +
                    "price DOUBLE PRECISION NOT NULL" +
                    ")");
            stmt.execute("CREATE TABLE orders (" +
                    "id SERIAL PRIMARY KEY," +
                    "customer VARCHAR(255) NOT NULL," +
                    "date TIMESTAMP NOT NULL," +
                    "status VARCHAR(255) NOT NULL" +
                    ")");

            stmt.execute("CREATE TABLE order_books (" +
                    "order_id BIGINT REFERENCES orders(id)," +
                    "book_id BIGINT REFERENCES books(id)," +
                    "PRIMARY KEY (order_id, book_id)" +
                    ")");
            stmt.execute("CREATE TABLE reviews (" +
                    "id SERIAL PRIMARY KEY," +
                    "book_id BIGINT REFERENCES books(id)," +
                    "reviewer VARCHAR(255) NOT NULL," +
                    "rating INT NOT NULL," +
                    "text TEXT NOT NULL" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
