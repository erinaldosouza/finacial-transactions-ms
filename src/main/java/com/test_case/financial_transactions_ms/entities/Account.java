package com.test_case.financial_transactions_ms.entities;

import com.test_case.financial_transactions_ms.dtos.AccountDTO;
import jakarta.persistence.*;
import lombok.*;

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
    private String uuid;
    private String number;
    @OneToOne(mappedBy = "account", cascade = CascadeType.PERSIST)
    private Customer customer;

    public AccountDTO toDTO() {
        return new AccountDTO(uuid, number, customer.getDocumentNumber());
    }

    @PrePersist
    private void generateUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

}
