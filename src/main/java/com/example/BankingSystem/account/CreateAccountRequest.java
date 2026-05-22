package com.example.BankingSystem.account;

// DTO used to receive account creation data from the client.
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
