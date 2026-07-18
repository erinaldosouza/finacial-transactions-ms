package com.test_case.financial_transactions_ms.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;
    private String documentNumber;
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @PrePersist
    private void generateUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

}

