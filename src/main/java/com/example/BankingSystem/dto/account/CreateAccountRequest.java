package com.example.BankingSystem.dto.account;

public class CreateAccountRequest {

    private String accountType;

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
