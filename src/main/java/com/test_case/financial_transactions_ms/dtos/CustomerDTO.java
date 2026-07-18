package com.test_case.financial_transactions_ms.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test_case.financial_transactions_ms.entities.Customer;
import org.hibernate.validator.constraints.Length;

public record CustomerDTO(
        @JsonProperty("document_number")
        @Length(min=11, max=11, message = "Document number must have exactly 11 characters")
        String documentNumber) implements AppDTO<Customer> {

    @Override
    public Customer toEntity() {
        return Customer
                .builder()
                .documentNumber(this.documentNumber)
                .build();
    }

}
