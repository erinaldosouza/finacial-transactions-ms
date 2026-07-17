package com.test_case.financial_transactions_ms.repositories;

import com.test_case.financial_transactions_ms.entities.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends AppJpaRepository<Transaction, Long> {

}
