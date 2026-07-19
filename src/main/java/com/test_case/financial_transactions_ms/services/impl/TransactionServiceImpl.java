package com.test_case.financial_transactions_ms.services.impl;

import com.test_case.financial_transactions_ms.entities.Transaction;
import com.test_case.financial_transactions_ms.enums.OperationType;
import com.test_case.financial_transactions_ms.repositories.AccountRepository;
import com.test_case.financial_transactions_ms.repositories.TransactionRepository;
import com.test_case.financial_transactions_ms.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Transaction create(Transaction transaction) {

        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction value must be greater than zero");
        }

        if(!OperationType.CREDIT_VOUCHER.equals(transaction.getOperationType())) {
            transaction.setAmount(transaction.getAmount().negate());
        }

        var account = accountRepository.findByExternalId(transaction.getAccount().getExternalId()).orElseThrow();
        transaction.setAccount(account);

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElseThrow();
    }

    @Override
    public Transaction findByExternalId(String uuid) {
        return transactionRepository.findByExternalId(uuid).orElseThrow();
    }
}
