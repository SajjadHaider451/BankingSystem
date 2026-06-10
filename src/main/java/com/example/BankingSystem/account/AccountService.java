package com.example.BankingSystem.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.BankingSystem.audit.AuditLogService;
import com.example.BankingSystem.exception.AccountNotFoundException;
import com.example.BankingSystem.exception.UnauthorizedAccessException;
import com.example.BankingSystem.exception.UserNotFoundException;
import com.example.BankingSystem.user.UserRepository;
import com.example.BankingSystem.user.Users;

/**
 * Service class containing the business logic for bank account operations, 
 * such as creating new accounts and retrieving account data.
 * 
 * business logic layer of the banking system for anything related to accounts
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository,
                          AuditLogService auditLogService) {

        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
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
        auditLogService.log(
                "ACCOUNT_CREATED",
                user.getEmail(),
                "Created account "
                        + account.getAccountNumber()
        );
        
        account.setStatus(AccountStatus.ACTIVE);

        return mapToResponse(savedAccount);
    }

    public AccountResponse getAccountById(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));
        
        validateAccountOwnership(account); 
        
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
                account.getUser().getId(),
                account.getStatus().name()
                
        );
    }
    
    public void validateAccountOwnership(Account account) {
    	
    	// retrieves the user that JWT authenticated
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        
        // gets email from jwt
        String loggedInEmail =
                authentication.getName();
        
        // gets owner of account from database
        String accountOwnerEmail =
                account.getUser().getEmail();

        /*
         * Admins bypass ownership checks
         */
        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("ROLE_ADMIN"));

        if (isAdmin) {
            return;
        }

        /*
         * User must own account
         */
        if (!loggedInEmail.equals(accountOwnerEmail)) {

            throw new UnauthorizedAccessException(
                    "You do not have access to this account");
        }
    }
}
