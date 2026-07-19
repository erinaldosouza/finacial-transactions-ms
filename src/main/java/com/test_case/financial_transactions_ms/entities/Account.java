package com.test_case.financial_transactions_ms.entities;

import com.test_case.financial_transactions_ms.dtos.AccountDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="accounts")
public class Account implements AppEntity<AccountDTO, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String externalId;
    private String number;
    @OneToOne(mappedBy = "account", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Customer customer;
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    public AccountDTO toDTO() {
        return new AccountDTO(externalId, number, customer.getDocumentNumber());
    }

    @PrePersist
    private void generateExternalId() {
        this.externalId = UUID.randomUUID().toString();
    }

}
