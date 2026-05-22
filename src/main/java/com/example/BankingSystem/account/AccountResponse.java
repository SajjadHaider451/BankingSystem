package com.example.BankingSystem.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO Class used to safely send account information back to the client. 
public class AccountResponse {

    private Long id;
    private int accountNumber;
    private BigDecimal balance;
    private String accountType;
    private LocalDateTime createdAt;
    private Long userId;

    public AccountResponse() {
    }

    public AccountResponse(Long id,
                           int accountNumber,
                           BigDecimal balance,
                           String accountType,
                           LocalDateTime createdAt,
                           Long userId) {

        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
