package com.test_case.financial_transactions_ms.dtos;

import com.test_case.financial_transactions_ms.entities.Account;
import com.test_case.financial_transactions_ms.entities.Customer;
import jakarta.persistence.JoinColumn;

public record AccountDTO(

        String uuid,
        @JoinColumn(name="documentNumber") String customerDocumentNumber)  implements AppDTO<Account> {

    @Override
    public Account toEntity() {
       return Account
               .builder()
               .customer(Customer
                        .builder()
                        .documentNumber(this.customerDocumentNumber)
                        .build())
               .build();
    }
}
