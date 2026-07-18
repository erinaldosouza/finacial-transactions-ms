package com.test_case.financial_transactions_ms.services;

import com.test_case.financial_transactions_ms.entities.Account;
import com.test_case.financial_transactions_ms.entities.Customer;
import com.test_case.financial_transactions_ms.exceptions.ResourceAlreadyExistsException;
import com.test_case.financial_transactions_ms.repositories.AccountRepository;
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
        Customer customer = new Customer();
        customer.setDocumentNumber("12345678900");

        Account account = new Account();
        account.setCustomer(customer);

        when(customerService.existsByDocumentNumber("12345678900")).thenReturn(false);

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account result = service.create(account);

        assertNotNull(result.getNumber());
        assertEquals(9, result.getNumber().length());
        assertSame(result, customer.getAccount());

        verify(accountRepository).save(account);
    }

    @Test
    void shouldThrowExceptionWhenCustomerAlreadyHasAccount() {

        Customer customer = new Customer();
        customer.setDocumentNumber("12345678900");

        Account account = new Account();
        account.setCustomer(customer);

        when(customerService.existsByDocumentNumber("12345678900"))
                .thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> service.create(account));

        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldFindAccountById() {

        Account account = new Account();

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        Account result = service.findById(1L);

        assertSame(account, result);
    }

    @Test
    void shouldThrowExceptionWhenAccountIdDoesNotExist() {

        when(accountRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                java.util.NoSuchElementException.class,
                () -> service.findById(1L));
    }

    @Test
    void shouldFindAccountByUuid() {

        Account account = new Account();

        when(accountRepository.findByUuid("uuid"))
                .thenReturn(Optional.of(account));

        Account result = service.findByUuid("uuid");

        assertSame(account, result);
    }

    @Test
    void shouldThrowExceptionWhenAccountUuidDoesNotExist() {

        when(accountRepository.findByUuid("uuid"))
                .thenReturn(Optional.empty());

        assertThrows(
                java.util.NoSuchElementException.class,
                () -> service.findByUuid("uuid"));
    }
}
