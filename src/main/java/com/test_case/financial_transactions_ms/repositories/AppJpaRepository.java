package com.test_case.financial_transactions_ms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface AppJpaRepository<T, ID> extends JpaRepository<T, ID> {
    Optional<T> findByExternalId(String externalId);
    Boolean existsByExternalId(String externalId);
}
