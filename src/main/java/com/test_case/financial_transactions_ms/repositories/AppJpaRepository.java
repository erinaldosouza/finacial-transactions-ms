package com.test_case.financial_transactions_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AppJpaRepository<T, ID> extends JpaRepository<T, ID> {
    T findByUuid(String uuid);
}
