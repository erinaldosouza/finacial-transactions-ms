package com.test_case.financial_transactions_ms.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test_case.financial_transactions_ms.entities.Transaction;
import com.test_case.financial_transactions_ms.enums.OperationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionDTO (
        @JsonProperty("transaction_id")
        String uuid,

        @JsonProperty("account_id")
        @NotBlank(message = "Account id/uuid must be valid account id")
        String accountUuid,

        @JsonProperty("operation_type_id")
        @NotNull(message = "Operation type must be a valid operation type")
        OperationType operationType,

        @Min(value = 1, message = "Amount must be at least 1 USD" )
        BigDecimal amount)
implements AppDTO<Transaction> {

    @Override
    public Transaction toEntity() {
        return Transaction
                .builder()
                .accountUuid(accountUuid)
                .operationType(operationType)
                .amount(amount)
                .build();
    }
}
