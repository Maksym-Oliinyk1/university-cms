package ua.com.foxminded.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import javax.sql.DataSource;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(DataSourceConfig.class)
@ComponentScan("ua.com.foxminded.repository")
public abstract class BaseTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16");

    @Autowired
    protected DataSource dataSource;

    @BeforeAll
    public static void setup() {
        postgreSQLContainer.start();
    }

    @AfterAll
    protected static void tearUp() {
        postgreSQLContainer.close();
    }

    @BeforeEach
    protected void initStartScript() {
        Resource scriptDataSchema = new ClassPathResource("/schema.sql");
        Resource scriptTestData = new ClassPathResource("/generate_data_for_tests.sql");
        ResourceDatabasePopulator populator =
                new ResourceDatabasePopulator(scriptDataSchema, scriptTestData);
        populator.execute(dataSource);
    }

    @AfterEach
    protected void clearDatabase() {
        Resource script = new ClassPathResource("/clear_tables.sql");
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(script);
        populator.execute(dataSource);
    }
}
