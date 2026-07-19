package com.test_case.financial_transactions_ms.services;

public interface AppService<T> {
    T create(T t);
    T findById(Long id);
    T findByExternalId(String externalId);

}
