package com.test_case.financial_transactions_ms.entities;

import com.test_case.financial_transactions_ms.dtos.TransactionDTO;
import com.test_case.financial_transactions_ms.enums.OperationType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name="transactions")
public class Transaction implements AppEntity<TransactionDTO, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    private String uuid;
    private String accountUuid;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    private BigDecimal amount;
    private LocalDateTime dateTime;

    public TransactionDTO toDTO() {
        return new TransactionDTO(uuid, accountUuid, operationType, amount);
    }
}
