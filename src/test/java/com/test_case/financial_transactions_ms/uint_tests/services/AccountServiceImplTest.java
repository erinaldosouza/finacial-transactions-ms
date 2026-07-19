package com.test_case.financial_transactions_ms.uint_tests.services;

import com.test_case.financial_transactions_ms.entities.Account;
import com.test_case.financial_transactions_ms.entities.Customer;
import com.test_case.financial_transactions_ms.exceptions.ResourceAlreadyExistsException;
import com.test_case.financial_transactions_ms.repositories.AccountRepository;
import com.test_case.financial_transactions_ms.services.CustomerService;
import com.test_case.financial_transactions_ms.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl service;

    @Test
    void shouldCreateAccountSuccessfully() {
        // given
        Customer customer = new Customer();
        customer.setDocumentNumber("12345678900");

        Account account = new Account();
        account.setCustomer(customer);

        // when

        when(customerService.existsByDocumentNumber("12345678900")).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(customerService.create(account.getCustomer())).thenAnswer(invocation -> invocation.getArgument(0));
        Account result = service.create(account);

        // then
        assertNotNull(result.getNumber());
        assertEquals(9, result.getNumber().length());
        assertSame(result, customer.getAccount());

        verify(accountRepository).save(account);
    }

    @Test
    void shouldThrowExceptionWhenCustomerAlreadyHasAccount() {
        // given
        Customer customer = new Customer();
        customer.setDocumentNumber("12345678900");

        Account account = new Account();
        account.setCustomer(customer);

        // when
        when(customerService.existsByDocumentNumber("12345678900"))
                .thenReturn(true);

        // then
        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> service.create(account));

        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldFindAccountById() {
        // given
        Account account = new Account();

        // when
        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        Account result = service.findById(1L);

        // then
        assertSame(account, result);
    }

    @Test
    void shouldThrowExceptionWhenAccountIdDoesNotExist() {

        // when
        when(accountRepository.findById(1L))
                .thenReturn(Optional.empty());

        // then
        assertThrows(
                java.util.NoSuchElementException.class,
                () -> service.findById(1L));
    }

    @Test
    void shouldFindAccountByUuid() {

        // given
        Account account = new Account();

        // when
        when(accountRepository.findByExternalId("uuid"))
                .thenReturn(Optional.of(account));

        Account result = service.findByExternalId("uuid");

        // then
        assertSame(account, result);
    }

    @Test
    void shouldThrowExceptionWhenAccountUuidDoesNotExist() {

        // when
        when(accountRepository.findByExternalId("uuid"))
                .thenReturn(Optional.empty());

        // then
        assertThrows(
                java.util.NoSuchElementException.class,
                () -> service.findByExternalId("uuid"));
    }
}
