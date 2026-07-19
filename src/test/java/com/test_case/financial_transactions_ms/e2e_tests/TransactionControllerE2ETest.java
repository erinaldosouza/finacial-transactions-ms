package com.test_case.financial_transactions_ms.e2e_tests;

import com.test_case.financial_transactions_ms.dtos.TransactionDTO;
import com.test_case.financial_transactions_ms.e2e_tests.configs.AbstractE2ETest;
import com.test_case.financial_transactions_ms.enums.OperationType;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class TransactionControllerE2ETest extends AbstractE2ETest {

    private static final String TRANSACTION_EXTERNAL_ID = "53574399-6671-46eb-8f08-ac15e4ec989c";
    private static final String ACCOUNT_EXTERNAL_ID = "53574399-6671-46eb-8f08-ac15e4ec989b";

    @Test
    void shouldCreateTransactionWithValidData() {

        TransactionDTO request = new TransactionDTO(
                null,
                ACCOUNT_EXTERNAL_ID,
                OperationType.CREDIT_VOUCHER,
                new BigDecimal("100.00")
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/transactions")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/v1/transactions/"));
    }

    @Test
    void shouldReturnTransactionByExternalId() {

        given()
                .when()
                .get("/v1/transactions/{externalId}", TRANSACTION_EXTERNAL_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("transaction_id", equalTo(TRANSACTION_EXTERNAL_ID));
    }

    @Test
    void shouldReturn404WhenTransactionDoesNotExist() {

        given()
                .when()
                .get("/v1/transactions/{externalId}", "invalid-transaction-id")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidAccountIds")
    void shouldReturnBadRequestWhenAccountIdIsInvalid(String accountId) {

        TransactionDTO request = new TransactionDTO(
                null,
                accountId,
                OperationType.CREDIT_VOUCHER,
                new BigDecimal("100.00")
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Validation Error"))
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("errors", notNullValue())
                .body("errors", hasItem(containsString("Account id must be valid account id")));
    }

    @Test
    void shouldReturnBadRequestWhenOperationTypeIsNull() {

        TransactionDTO request = new TransactionDTO(
                null,
                ACCOUNT_EXTERNAL_ID,
                null,
                new BigDecimal("100.00")
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Validation Error"))
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("errors", notNullValue())
                .body("errors", hasItem(containsString("Operation type must be a valid operation type")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-100", "-1"})
    void shouldReturnBadRequestWhenAmountIsZeroOrNegative(String amount) {

        TransactionDTO request = new TransactionDTO(
                null,
                ACCOUNT_EXTERNAL_ID,
                OperationType.CREDIT_VOUCHER,
                new BigDecimal(amount)
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Validation Error"))
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("errors", notNullValue())
                .body("errors", hasItem(containsString("Amount must be at least 1 USD")));
    }

    @Test
    void shouldReturnBadRequestWhenAmountIsNull() {

        TransactionDTO request = new TransactionDTO(
                null,
                ACCOUNT_EXTERNAL_ID,
                OperationType.CREDIT_VOUCHER,
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Validation Error"))
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("errors", notNullValue());
    }

    private static Stream<String> provideInvalidAccountIds() {
        return Stream.of(null, "");
    }


}
