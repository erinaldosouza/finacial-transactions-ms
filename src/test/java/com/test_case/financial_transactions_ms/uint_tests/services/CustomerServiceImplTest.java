package com.test_case.financial_transactions_ms.uint_tests.services;

import com.test_case.financial_transactions_ms.entities.Customer;
import com.test_case.financial_transactions_ms.repositories.CustomerRepository;
import com.test_case.financial_transactions_ms.services.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl service;

    @Test
    void shouldReturnTrueWhenCustomerExistsByDocumentNumber() {

        // given
        String documentNumber = "12345678900";

        // when
        when(customerRepository.existsByDocumentNumber(documentNumber))
                .thenReturn(true);

        Boolean result = service.existsByDocumentNumber(documentNumber);

        // then
        assertTrue(result);

        verify(customerRepository).existsByDocumentNumber(documentNumber);
    }

    @Test
    void shouldReturnFalseWhenCustomerDoesNotExistByDocumentNumber() {

        // given
        String documentNumber = "12345678900";

        // when
        when(customerRepository.existsByDocumentNumber(documentNumber))
                .thenReturn(false);

        Boolean result = service.existsByDocumentNumber(documentNumber);

        // then
        assertFalse(result);

        verify(customerRepository).existsByDocumentNumber(documentNumber);
    }

    @Test
    void shouldCreateCustomerSuccessfully() {

        // given
        Customer customer = Customer.builder().documentNumber("12345678900").build();
        when(customerRepository.save(customer)).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Customer result = service.create(customer);

        // then
        assertNotNull(result);
        assertEquals("12345678900", result.getDocumentNumber());
        verify(customerRepository).save(customer);

    }

    @Test
    void shouldThrowExceptionWhenFindByIdIsCalled() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.findById(1L));

        assertEquals("Not implemented", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFindByExternalIdIsCalled() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.findByExternalId("uuid"));

        assertEquals("Not implemented", exception.getMessage());
    }
}