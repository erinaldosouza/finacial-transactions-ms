package com.test_case.financial_transactions_ms.e2e_tests.configs;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractE2ETest extends PostgreSQLContainerConfig {

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

}