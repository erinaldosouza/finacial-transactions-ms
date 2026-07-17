package com.test_case.financial_transactions_ms.services;

public interface AppService<T> {
    T save(T t);
    T findById(Long id);
    T findByUuid(String uuid);

}
