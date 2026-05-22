package com.example.BankingSystem.common.exception;

/**
 * Exception thrown when a requested bank account cannot be found in the system.
 */
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
