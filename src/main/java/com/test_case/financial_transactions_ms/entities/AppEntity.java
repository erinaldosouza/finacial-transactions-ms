package com.test_case.financial_transactions_ms.entities;

public interface AppEntity<T, ID> {
    ID getId();
    String getExternalId();
    T toDTO();
}
