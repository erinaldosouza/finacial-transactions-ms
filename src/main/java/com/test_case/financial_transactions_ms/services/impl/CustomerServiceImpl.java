package com.test_case.financial_transactions_ms.services.impl;

import com.test_case.financial_transactions_ms.entities.Customer;
import com.test_case.financial_transactions_ms.repositories.CustomerRepository;
import com.test_case.financial_transactions_ms.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer findById(Long id) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Customer findByExternalId(String uuid) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Boolean existsByDocumentNumber(String documentNumber) {
        return customerRepository.existsByDocumentNumber(documentNumber);
    }
}
