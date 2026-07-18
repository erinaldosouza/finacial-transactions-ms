package com.test_case.financial_transactions_ms.services;

import com.test_case.financial_transactions_ms.entities.Customer;
import com.test_case.financial_transactions_ms.repositories.CustomerRepository;
import com.test_case.financial_transactions_ms.services.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl service;

    @Test
    void shouldReturnTrueWhenCustomerExistsByDocumentNumber() {

        String documentNumber = "12345678900";

        when(customerRepository.existsByDocumentNumber(documentNumber))
                .thenReturn(true);

        Boolean result = service.existsByDocumentNumber(documentNumber);

        assertTrue(result);

        verify(customerRepository).existsByDocumentNumber(documentNumber);
    }

    @Test
    void shouldReturnFalseWhenCustomerDoesNotExistByDocumentNumber() {

        String documentNumber = "12345678900";

        when(customerRepository.existsByDocumentNumber(documentNumber))
                .thenReturn(false);

        Boolean result = service.existsByDocumentNumber(documentNumber);

        assertFalse(result);

        verify(customerRepository).existsByDocumentNumber(documentNumber);
    }

    @Test
    void shouldThrowExceptionWhenCreateIsCalled() {

        Customer customer = new Customer();

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.create(customer));

        assertEquals("Not implemented", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFindByIdIsCalled() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.findById(1L));

        assertEquals("Not implemented", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFindByUuidIsCalled() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.findByUuid("uuid"));

        assertEquals("Not implemented", exception.getMessage());
    }
}