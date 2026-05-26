package com.example.BankingSystem.account;

import jakarta.validation.constraints.NotBlank;

// DTO used to receive account creation data from the client.
public class CreateAccountRequest {

    @NotBlank(message = "Account type is required")
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
