package com.example.BankingSystem.exception;

/**
 * Exception thrown when an account does not have enough balance to complete a transaction.
 */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
