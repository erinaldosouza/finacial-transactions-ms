package com.test_case.financial_transactions_ms.services.impl;

import com.test_case.financial_transactions_ms.entities.Transaction;
import com.test_case.financial_transactions_ms.repositories.TransactionRepository;
import com.test_case.financial_transactions_ms.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public Transaction findByUuid(String uuid) {
        return transactionRepository.findByUuid(uuid).orElseThrow();
    }
}
