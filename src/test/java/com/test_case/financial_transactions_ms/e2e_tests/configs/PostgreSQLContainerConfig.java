package com.test_case.financial_transactions_ms.e2e_tests.configs;


import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class PostgreSQLContainerConfig {

    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:18")
                    .withDatabaseName("financial-transactions-test-db")
                    .withUsername("postgres")
                    .withPassword("postgres");

    static {
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url",
                POSTGRES::getJdbcUrl);

        registry.add("spring.datasource.username",
                POSTGRES::getUsername);

        registry.add("spring.datasource.password",
                POSTGRES::getPassword);

        registry.add("spring.datasource.driver-class-name",
                POSTGRES::getDriverClassName);
    }

}
