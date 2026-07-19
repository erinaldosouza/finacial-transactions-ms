package com.test_case.financial_transactions_ms.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test_case.financial_transactions_ms.entities.Account;
import com.test_case.financial_transactions_ms.entities.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record AccountDTO(

        @JsonProperty("account_id")
        String externalId,

        @JsonProperty("account_number")
        String accountNumber,

        @JsonProperty("document_number")
        @NotBlank(message = " Document number must be a valid document number")
        @Length(min=11, max=11, message = "Document number must have exactly 11 digits")
        @Pattern(regexp = "^[0-9]+$", message = "Document number must contain only numbers")
        String customerDocumentNumber)  implements AppDTO<Account> {

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
