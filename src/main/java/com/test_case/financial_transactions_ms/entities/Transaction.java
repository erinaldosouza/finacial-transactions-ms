package com.test_case.financial_transactions_ms.entities;

import com.test_case.financial_transactions_ms.dtos.TransactionDTO;
import com.test_case.financial_transactions_ms.enums.OperationType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="transactions")
public class Transaction implements AppEntity<TransactionDTO, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    private String externalId;
    @ManyToOne(fetch = FetchType.EAGER)
    private Account account;
    @Column(name="operation_type_id")
    private OperationType operationType;
    private BigDecimal amount;
    private LocalDateTime dateTime;

    public TransactionDTO toDTO() {
        return new TransactionDTO(externalId, account.getExternalId(), operationType, amount);
    }

    @PrePersist
    private void generateExternalId() {
        this.externalId = UUID.randomUUID().toString();
        this.dateTime = LocalDateTime.now();
    }

}
