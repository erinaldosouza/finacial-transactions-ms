package com.test_case.financial_transactions_ms.repositories;

import com.test_case.financial_transactions_ms.entities.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends AppJpaRepository<Account, Long> {

}
