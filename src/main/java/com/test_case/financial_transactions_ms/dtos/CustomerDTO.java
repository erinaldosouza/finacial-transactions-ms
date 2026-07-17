package com.test_case.financial_transactions_ms.dtos;

import com.test_case.financial_transactions_ms.entities.Customer;

public record CustomerDTO(String documentNumber) implements AppDTO<Customer> {

    @Override
    public Customer toEntity() {
        return Customer
                .builder()
                .documentNumber(this.documentNumber)
                .build();
    }

}
