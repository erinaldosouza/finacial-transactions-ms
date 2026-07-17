package com.test_case.financial_transactions_ms.services;

import com.test_case.financial_transactions_ms.entities.Customer;

public interface CustomerService extends AppService<Customer> {
    Boolean existsByDocumentNumber(String documentNumber);
}
