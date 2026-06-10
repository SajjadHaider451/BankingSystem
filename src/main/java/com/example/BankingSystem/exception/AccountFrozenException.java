package com.example.BankingSystem.exception;

public class AccountFrozenException extends RuntimeException {

    public AccountFrozenException(
            String message) {

        super(message);
    }
}
