package com.test_case.financial_transactions_ms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OperationType {
    NORMAL_PURCHASE(1),
    PURCHASE_WITH_INSTALLMENTS(2),
    WITHDRAW(3),
    CREDIT_VOUCHER(4);

    private final int operationId;

    OperationType(int operationId) {
        this.operationId = operationId;
    }

    @JsonValue
    public int getOperationId() {
        return operationId;
    }

    @JsonCreator
    public static OperationType fromValue(int operationId) {
        for (OperationType operation : OperationType.values()) {
            if (operation.getOperationId() == operationId) {
                return operation;
            }
        }
        throw new IllegalArgumentException("Unknown OperationType: " + operationId);
    }

}
