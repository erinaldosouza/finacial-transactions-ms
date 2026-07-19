package com.test_case.financial_transactions_ms.uint_tests.services;

import com.test_case.financial_transactions_ms.entities.Account;
import com.test_case.financial_transactions_ms.entities.Transaction;
import com.test_case.financial_transactions_ms.enums.OperationType;
import com.test_case.financial_transactions_ms.repositories.AccountRepository;
import com.test_case.financial_transactions_ms.repositories.TransactionRepository;
import com.test_case.financial_transactions_ms.services.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl service;

    @ParameterizedTest
    @EnumSource(OperationType.class)
    void shouldCreateTransaction(OperationType operationType) {

        // given
        var amount = new BigDecimal("100.00");
        Account account = Account.builder().externalId("account-externalId").build();
        Transaction transaction = new Transaction();
        transaction.setExternalId("transaction-externalId");
        transaction.setAmount(amount);
        transaction.setOperationType(operationType);
        transaction.setAccount(account);

        // when
        when(accountRepository.findByExternalId(account.getExternalId()))
                .thenReturn(Optional.of(account));


        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = service.create(transaction);

        BigDecimal expected = operationType == OperationType.CREDIT_VOUCHER
                ? amount
                : amount.negate();

        // then
        assertEquals(expected, result.getAmount());

        verify(transactionRepository).save(transaction);
    }

    @ParameterizedTest
    @MethodSource("invalidAmounts")
    void shouldThrowExceptionWhenAmountIsZeroOrNegative(BigDecimal amount) {

        // given
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);

        // then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(transaction));

        assertEquals(
                "Transaction value must be greater than zero",
                exception.getMessage());

        verifyNoInteractions(accountRepository);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {

        // given
        Account account = Account.builder().externalId("account-externalId").build();
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setExternalId("externalId");
        transaction.setAmount(new BigDecimal("10"));
        transaction.setOperationType(OperationType.CREDIT_VOUCHER);

        // then
        assertThrows(
                NoSuchElementException.class,
                () -> service.create(transaction));

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldFindTransactionById() {

        // given
        Transaction transaction = new Transaction();

        // when
        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(transaction));

        Transaction result = service.findById(1L);

        // then
        assertSame(transaction, result);
    }

    @Test
    void shouldThrownExceptionWhenTransactionIdDoesNotExist() {

        // when
        when(transactionRepository.findById(1L))
                .thenReturn(Optional.empty());

        // then
        assertThrows(
                NoSuchElementException.class,
                () -> service.findById(1L));
    }

    @Test
    void shouldFindTransactionByExternalId() {

        // given
        Transaction transaction = new Transaction();

        //when
        when(transactionRepository.findByExternalId("externalId"))
                .thenReturn(Optional.of(transaction));

        Transaction result = service.findByExternalId("externalId");

        //then
        assertSame(transaction, result);
    }

    @Test
    void shouldThrowExceptionWhenTransactionExternalIdDoesNotExist() {

        // when
        when(transactionRepository.findByExternalId("externalId"))
                .thenReturn(Optional.empty());

        // then
        assertThrows(
                NoSuchElementException.class,
                () -> service.findByExternalId("externalId"));
    }

    private static Stream<BigDecimal> invalidAmounts() {
        return Stream.of(
                BigDecimal.ZERO,
                BigDecimal.valueOf(-1)
        );
    }
}
