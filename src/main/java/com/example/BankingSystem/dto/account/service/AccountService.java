package com.example.BankingSystem.dto.account.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.BankingSystem.Entity.Account;
import com.example.BankingSystem.Entity.Users;
import com.example.BankingSystem.dto.account.AccountResponse;
import com.example.BankingSystem.dto.account.CreateAccountRequest;
import com.example.BankingSystem.exeception.AccountNotFoundException;
import com.example.BankingSystem.exeception.UserNotFoundException;
import com.example.BankingSystem.repo.AccountRepository;
import com.example.BankingSystem.repo.UserRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository) {

        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public AccountResponse createAccount(Long userId,
                                         CreateAccountRequest request) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        Account account = new Account();

        account.setAccountType(request.getAccountType());
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());
        account.setAccountNumber(generateAccountNumber());
        account.setUser(user);

        Account savedAccount = accountRepository.save(account);

        return mapToResponse(savedAccount);
    }

    public AccountResponse getAccountById(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        return mapToResponse(account);
    }

    public List<AccountResponse> getAccountsByUser(Long userId) {

        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private int generateAccountNumber() {

        Random random = new Random();

        return 100000 + random.nextInt(900000);
    }

    private AccountResponse mapToResponse(Account account) {

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getCreatedAt(),
                account.getUser().getId()
        );
    }
}
