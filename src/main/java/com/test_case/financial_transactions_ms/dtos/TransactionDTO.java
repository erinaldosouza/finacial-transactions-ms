package com.test_case.financial_transactions_ms.dtos;

import com.test_case.financial_transactions_ms.entities.Transaction;
import com.test_case.financial_transactions_ms.enums.OperationType;

import java.math.BigDecimal;

public record TransactionDTO (String uuid, String accountUuid, OperationType operationType, BigDecimal amount)
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
