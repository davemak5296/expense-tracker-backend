package com.codewithflow.exptracker;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public abstract class AbstractTestContainer {

    @Container
    private static final PostgreSQLContainer postgresSQLContainer = new PostgreSQLContainer("postgres:15.4");

    @DynamicPropertySource
    private static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgresSQLContainer::getPassword);
    }

    @BeforeAll
    static void beforeALL() {
        Flyway flyway = Flyway.configure().dataSource(
                postgresSQLContainer.getJdbcUrl(),
                postgresSQLContainer.getUsername(),
                postgresSQLContainer.getPassword())
            .load();

        flyway.migrate();
    }

    @Test
    void canStartPostgresDB() {
        assertThat(postgresSQLContainer.isRunning()).isTrue();
        assertThat(postgresSQLContainer.isCreated()).isTrue();
    }

//    protected static DataSource getDataSource() {
//        return DataSourceBuilder.create()
//                .url(postgresSQLContainer.getJdbcUrl())
//                .username(postgresSQLContainer.getUsername())
//                .password(postgresSQLContainer.getPassword())
//                .driverClassName(postgresSQLContainer.getDriverClassName())
//                .build();
//
//    }
}
