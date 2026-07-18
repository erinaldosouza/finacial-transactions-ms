package com.test_case.financial_transactions_ms.exceptions;

public class ResourceDoesntExistsException extends RuntimeException {
    public ResourceDoesntExistsException(String message) {
        super(message);
    }
}