package com.test_case.financial_transactions_ms.services;

import com.test_case.financial_transactions_ms.entities.Transaction;
import com.test_case.financial_transactions_ms.enums.OperationType;
import com.test_case.financial_transactions_ms.exceptions.ResourceDoesntExistsException;
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

        var amount = new BigDecimal("100.00");
        Transaction transaction = new Transaction();
        transaction.setAccountUuid("account-uuid");
        transaction.setAmount(amount);
        transaction.setOperationType(operationType);

        when(accountRepository.existsByUuid("account-uuid"))
                .thenReturn(true);

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = service.create(transaction);

        BigDecimal expected = operationType == OperationType.CREDIT_VOUCHER
                ? amount
                : amount.negate();

        assertEquals(expected, result.getAmount());

        verify(transactionRepository).save(transaction);
    }
    @ParameterizedTest
    @MethodSource("invalidAmounts")
    void shouldThrowExceptionWhenAmountIsZeroOrNegative(BigDecimal amount) {

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);

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

        Transaction transaction = new Transaction();
        transaction.setAccountUuid("uuid");
        transaction.setAmount(new BigDecimal("10"));
        transaction.setOperationType(OperationType.CREDIT_VOUCHER);

        when(accountRepository.existsByUuid("uuid"))
                .thenReturn(false);

        ResourceDoesntExistsException exception = assertThrows(
                ResourceDoesntExistsException.class,
                () -> service.create(transaction));

        assertEquals("Account not found", exception.getMessage());

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldFindTransactionById() {

        Transaction transaction = new Transaction();

        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(transaction));

        Transaction result = service.findById(1L);

        assertSame(transaction, result);
    }

    @Test
    void shouldThrownExceptionWhenTransactionIdDoesNotExist() {

        when(transactionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> service.findById(1L));
    }

    @Test
    void shouldFindTransactionByUuid() {

        Transaction transaction = new Transaction();

        when(transactionRepository.findByUuid("uuid"))
                .thenReturn(Optional.of(transaction));

        Transaction result = service.findByUuid("uuid");

        assertSame(transaction, result);
    }

    @Test
    void shouldThrowExceptionWhenTransactionUuidDoesNotExist() {

        when(transactionRepository.findByUuid("uuid"))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> service.findByUuid("uuid"));
    }

    private static Stream<BigDecimal> invalidAmounts() {
        return Stream.of(
                BigDecimal.ZERO,
                BigDecimal.valueOf(-1)
        );
    }
}
