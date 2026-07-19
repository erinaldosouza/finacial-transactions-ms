# Financial Transactions Microservice

A simple REST API service for managing customer accounts and financial transactions with comprehensive API documentation and test coverage.

> ℹ️ **Note:** This README and the project documentation are written in English because it is a requirement of the technical assessment. All source code, API documentation, and comments follow the same convention to maintain consistency throughout the project.

> ℹ️ > **Note:** This application is production-oriented, but it is not intended to be a production-ready system. Production-specific concerns beyond the assessment scope were not implemented.

## Overview

This microservice provides endpoints to:
- **Create and retrieve customer accounts** associated with document numbers
- **Create and retrieve transactions** linked to customer accounts

Built with **Spring Boot 4**, **PostgreSQL**, **Flyway migrations**, and comprehensive **e2e and unit tests with Junit, Mockito, Rest-Assured and Testcontainers**.

---

## Technical Stack

- **Language**: Java 25
- **Framework**: Spring Boot 4.1.0
- **Database**: PostgreSQL 18
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, Rest-Assured, Testcontainers
- **Database Migration**: Flyway
- **API Documentation**: OpenAPI 3.0 / Swagger UI, Postman Collection
- **ORM**: Spring Data / Hibernate JPA
- **Container**: Docker/Podma & Docker/Podman Compose

---

## Prerequisites

- Java 25+
- Maven 3.8+
- Docker & Docker Compose (optional, for containerized PostgreSQL)
- PostgreSQL 18 (if running without Docker)

---

## Quick Start

### 1. Clone and Navigate to Project

```bash
cd finacial-transactions-ms
```

### 2. Run the Application

**Option A — Using the app-run.sh script (Recommended):**
```bash
./run.sh
```
> ℹ️ This script handles PostgreSQL instantiation automatically.

**Option B — Using Maven:**
```bash
./mvnw spring-boot:run
```
> ⚠️ **Important:** PostgreSQL must be running.

**Option C — Build and run JAR:**
```bash
mvn clean package
java -jar target/financial_transactions_ms-0.0.1-SNAPSHOT.jar
```
> ⚠️ **Important:** PostgreSQL must be running.

### 5. Access OpenAPI Documentation

```
http://localhost:8080/swagger-ui/index.html
```

OpenAPI spec (YAML):
```
http://localhost:8080/openapi.yaml
```

---

## Data Model

### Accounts Table
Stores account information with unique external identifiers.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Internal identifier |
| number | VARCHAR(255) | | Account number |
| external_id | VARCHAR(255) | UNIQUE, NOT NULL | External UUID identifier |

### Customers Table
Stores customer information linked to accounts.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Internal identifier |
| account_id | BIGINT | UNIQUE, FOREIGN KEY → accounts | Link to account |
| document_number | VARCHAR(255) | | Customer document (CPF/ID) |
| external_id | VARCHAR(255) | UNIQUE, NOT NULL | External UUID identifier |

### Operation Types Table
Predefined transaction operation categories (loaded by V2__insert_operation_types.sql).

| ID | Description |
|----|-------------|
| 1 | NORMAL PURCHASE |
| 2 | PURCHASE WITH INSTALLMENTS |
| 3 | WITHDRAWAL |
| 4 | CREDIT VOUCHER |

### Transactions Table
Records financial transactions for each account.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Internal identifier |
| amount | NUMERIC(38,2) | | Transaction amount (negative for debit, positive for credit) |
| date_time | TIMESTAMP(6) | | Transaction timestamp |
| account_id | BIGINT | NOT NULL, FOREIGN KEY → accounts | Link to account |
| operation_type_id | INT | NOT NULL, FOREIGN KEY → operation_types | Type of transaction |
| external_id | VARCHAR(255) | UNIQUE, NOT NULL | External UUID identifier |

### Database Relationships

**Key Relationships:**
- Each **Account** can have one **Customer**
- Each **Account** can have multiple **Transactions**
- Each **Transaction** must reference an **Account** and an **OperationType**
- Each **OperationType** is shared across multiple **Transactions**

### Flyway Migrations

Database initialization is managed by Flyway:

| File | Purpose |
|------|---------|
| V1__initial_schema.sql | Creates all tables with constraints and foreign keys |
| V2__insert_operation_types.sql | Seeds operation_types with predefined values (1-4) |
| R__insert_test_data.sql | Repeatable migration that inserts test data for e2e tests |

Migrations run automatically on application startup (managed by `spring.flyway.enabled: true`).

---

## Testing

> ℹ️ **Note:** The first execution of the test suite may take several minutes while Maven dependencies and Testcontainers images are downloaded. This is expected.

### Run All Tests

```bash
mvn test
```

### Run Only E2E Tests

```bash
mvn -Dtest=*E2ETest test
```

### Run Only Unit Tests

```bash
mvn -Dtest=*ImplTest test
```

### Test Coverage

The project includes comprehensive test suites:

**E2E Tests (Rest-Assured + Testcontainers + PostgreSQL):**
- `AccountControllerE2ETest` — 8 scenarios (valid/invalid inputs, validation rules)
- `TransactionControllerE2ETest` — 9 scenarios (valid/invalid inputs, business rules)

**Unit Tests:**
- `CustomerServiceImplTest` — Service business logic
- `TransactionServiceImplTest` — Transaction creation and validation

**Test Data:**
- Flyway migration `R__insert_test_data.sql` loads test accounts and data for e2e tests
- PostgreSQL Testcontainer provides isolated test database per test class

---

## Docker Deployment

### Run with Docker Compose

```bash
docker-compose up -d
```

Stop:
```bash
docker-compose down
```

---

## Error Handling

All validation and application errors follow Problem Detail format:

```json
{
  "detail": "Human-readable message",
  "instance": "/v1/endpoint",
  "status": 400,
  "title": "Error Category",
  "timestamp": "2026-07-19T13:15:30.123456",
  "errors": ["Specific error 1", "Specific error 2"]
}
```

## Development Notes

### Key Design Decisions

1. **DTOs for Validation** — Input validation at DTO layer using Jakarta Validation annotations
2. **Service Layer** — Business logic isolated in services for testability
3. **Enum for Operation Types** — Type safety for transaction operations
4. **Global Exception Handler** — Centralized error handling with ProblemDetail format
5. **Testcontainers** — Isolated PostgreSQL instance per test class for true integration testing

### Adding New Features

1. **New Endpoint**: Create controller → add service → add repository (if needed)
2. **New Validation**: Add annotation to DTO field
3. **New Business Rule**: Add logic to service layer with tests
4. **Database Change**: Create new Flyway migration file

---

## License

MIT License — feel free to use this project as a reference or template.

---

**Happy coding!** 🚀

