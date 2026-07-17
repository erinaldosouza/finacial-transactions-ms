package com.test_case.financial_transactions_ms.services.impl;

import com.test_case.financial_transactions_ms.entities.Account;
import com.test_case.financial_transactions_ms.exceptions.CustomerAlreadyExistsException;
import com.test_case.financial_transactions_ms.repositories.AccountRepository;
import com.test_case.financial_transactions_ms.services.AccountService;
import com.test_case.financial_transactions_ms.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Account save(Account account) {
        var documentNumber = account.getCustomer().getDocumentNumber();
        var customer  = customerService.existsByDocumentNumber(documentNumber);
        if(customer) {
            throw new CustomerAlreadyExistsException(String.format(documentNumber, "Customer with document number: $s already exists"));
        }

        return accountRepository.save(account);
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    public Account findByUuid(String uuid) {
        return null;
    }
}
