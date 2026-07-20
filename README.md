# Financial Transactions Microservice

A simple REST API service for managing customer accounts and financial transactions with comprehensive API documentation and test coverage.

> ℹ️ **Note:** This README and the project documentation are written in English because it is a requirement of the technical assessment. All source code, API documentation, and comments follow the same convention to maintain consistency throughout the project.

> ℹ️ **Note:** This application is production-oriented, but it is not intended to be a production-ready system. Production-specific concerns beyond the assessment scope were not implemented.

## Overview

This microservice provides endpoints to:
- **Create and retrieve customer accounts** associated with document numbers
- **Create and retrieve transactions** linked to customer accounts

Built with **Spring Boot 4**, **PostgreSQL**, **Redis**, **Flyway migrations**, and comprehensive **e2e and unit tests with JUnit, Mockito, Rest-Assured and Testcontainers**.

---

## Technical Stack

- **Language**: Java 25
- **Framework**: Spring Boot 4.1.0
- **Database**: PostgreSQL 18
- **Cache / Idempotency Store**: Redis
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
- Docker & Docker Compose (optional, for containerization)
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
> ℹ️ This script handles application and environment startup automatically using containers.

**Option B — Using Maven:**
```bash
./mvnw spring-boot:run
```
> ⚠️ **Important:** PostgreSQL must be running.

**Option C — Build and run JAR:**
```bash
./mvnw clean package
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
## Idempotency

To prevent duplicate processing of write operations, all **HTTP POST** endpoints require an `Idempotency-Key` request header.

The application uses **Redis** to temporarily store processed idempotency keys and ensure that the same request is executed only once.

Example:

```http
POST /v1/transactions
Idempotency-Key: 9c1d9f77-3db6-4c90-95ef-b57d2d2dd2a4
Content-Type: application/json
{
  "accountExternalId": "a1b2c3d4-e5f6-7g8h-9i0j-k1l2m3n4o5p6",
  "operationTypeId": 1,
  "amount": -100.00
}
```

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

### Flyway Metadata Table

Flyway automatically creates and manages the `flyway_schema_history` table to track database migrations.

This table stores information about:
- Applied migration versions
- Migration execution timestamps
- Migration status (success/failure)
- Migration checksums to detect unexpected changes

The table is managed exclusively by Flyway and should not be modified manually.

| Column | Type | Description |
|--------|------|-------------|
| installed_rank | INT | Order in which migrations were applied |
| version | VARCHAR | Migration version identifier |
| description | VARCHAR | Migration description |
| type | VARCHAR | Migration type (e.g., SQL) |
| script | VARCHAR | Migration script name |
| checksum | INT | Migration checksum validation |
| installed_by | VARCHAR | Database user that executed the migration |
| installed_on | TIMESTAMP | Migration execution timestamp |
| execution_time | INT | Migration execution duration in milliseconds |
| success | BOOLEAN | Indicates whether the migration completed successfully |
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

### PostgreSQL Administration (pgAdmin)

A pgAdmin container is available for database administration when running the application through Docker/Podman Compose.

Access pgAdmin at:
- http://localhost:5050

Credentials:
- Email: admin@admin.com
- Password: admin

To connect to the PostgreSQL database from pgAdmin, use the following connection settings:
- Host: postgres
- Port: 5432
- Database: financial-transactions-db
- Username: postgres
- Password: postgres

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

The containerized environment includes:

- PostgreSQL 18
- pgAdmin 4 (optional)
- Redis

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
6. **Redis-based Idempotency** — POST requests require an `Idempotency-Key` header to prevent duplicate request processing during retries or network failures.

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

