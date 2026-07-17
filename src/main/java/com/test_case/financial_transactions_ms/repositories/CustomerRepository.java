package com.test_case.financial_transactions_ms.repositories;

import com.test_case.financial_transactions_ms.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Boolean existsByDocumentNumber(String documentNumber);
}
