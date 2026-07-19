package com.test_case.financial_transactions_ms.e2e_tests;

import com.test_case.financial_transactions_ms.dtos.AccountDTO;
import com.test_case.financial_transactions_ms.e2e_tests.configs.AbstractE2ETest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AccountControllerE2ETest extends AbstractE2ETest {

    private static final String ACCOUNT_EXTERNAL_ID = "53574399-6671-46eb-8f08-ac15e4ec989b";
    private static final String ACCOUNT_NUMBER = "12345678910";

    @Test
    void shouldCreateAccountWithValidDocumentNumber() {

        AccountDTO request = new AccountDTO(null, null, "12345678901");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/accounts")
                .then()
                .statusCode(201)
                .header("Location", containsString("/v1/accounts/"));
    }

    @Test
    void shouldReturnAccountByUuid() {

        given()
                .when()
                .get("/v1/accounts/{uuid}", ACCOUNT_EXTERNAL_ID)
                .then()
                .statusCode(200)
                .body("account_id", equalTo(ACCOUNT_EXTERNAL_ID))
                .body("account_number", equalTo(ACCOUNT_NUMBER));
    }

    @Test
    void shouldReturn404WhenAccountDoesNotExist() {

        given()
                .when()
                .get("/v1/accounts/{uuid}", "invalid-uuid")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturnBadRequestWhenDocumentNumberIsBlank() {

        AccountDTO request = new AccountDTO(null, null, "");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Validation Error"))
                .body("status", equalTo(400))
                .body("errors", notNullValue())
                .body("errors", hasItem(containsString("must be a valid document number")));
    }

    @Test
    void shouldReturnBadRequestWhenDocumentNumberIsNull() {

        AccountDTO request = new AccountDTO(null, null, null);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Validation Error"))
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("errors", notNullValue())
                .body("errors", hasItem(containsString("must be a valid document number")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "123456789012"})
    void shouldReturnBadRequestWhenDocumentNumberHasInvalidLength(String documentNumber) {

        AccountDTO request = new AccountDTO(null, null, documentNumber);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Validation Error"))
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("errors", notNullValue())
                .body("errors", hasItem(containsString("must have exactly 11 digits")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567890a", "abcdefghijk", "12345678 90", "123456789!0"})
    void shouldReturnBadRequestWhenDocumentNumberContainsNonNumericCharacters(String documentNumber) {

        AccountDTO request = new AccountDTO(null, null, documentNumber);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Validation Error"))
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("errors", notNullValue())
                .body("errors", hasItem(containsString("must contain only numbers")));
    }

}
