package com.test_case.financial_transactions_ms.services.impl;

import com.test_case.financial_transactions_ms.entities.Account;
import com.test_case.financial_transactions_ms.exceptions.ResourceAlreadyExistsException;
import com.test_case.financial_transactions_ms.repositories.AccountRepository;
import com.test_case.financial_transactions_ms.services.AccountService;
import com.test_case.financial_transactions_ms.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {
    private final CustomerService customerService;
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(CustomerService customerService, AccountRepository accountRepository) {
        this.customerService = customerService;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account create(Account account) {
        var documentNumber = account.getCustomer().getDocumentNumber();
        var customer  = customerService.existsByDocumentNumber(documentNumber);
        if(customer) {
            throw new ResourceAlreadyExistsException(String.format("Customer with document number: %s already have an account", documentNumber));
        }

        var createdCustomer = customerService.create(account.getCustomer());
        createdCustomer.setAccount(account);
        account.setCustomer(createdCustomer);

        String accountNumber = String.format("%09d", new Random().nextInt(1_000_000_000));
        account.setNumber(accountNumber);

        return accountRepository.save(account);
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    public Account findByExternalId(String externalId) {
        return accountRepository.findByExternalId(externalId).orElseThrow();
    }
}
