package com.example.BankingSystem.admin;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankingSystem.account.Account;
import com.example.BankingSystem.account.AccountRepository;
import com.example.BankingSystem.account.AccountStatus;
import com.example.BankingSystem.exception.AccountNotFoundException;
import com.example.BankingSystem.exception.UserNotFoundException;
import com.example.BankingSystem.user.UserRepository;
import com.example.BankingSystem.user.Users;
import com.example.BankingSystem.audit.AuditLogService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AuditLogService auditLogService;

    public AdminController(UserRepository userRepository, AuditLogService auditLogService, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
        this.accountRepository = accountRepository;
    }

    /*
     * ONLY ADMINS CAN ACCESS
     */
    // ONLY authenticated admins can access this endpoint
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/freeze/{accountId}")
    public ResponseEntity<String> freezeAccount(@PathVariable Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found"));

        account.setStatus(AccountStatus.FROZEN);

        accountRepository.save(account);

        auditLogService.log(
                "ACCOUNT_FROZEN",
                account.getUser().getEmail(),
                "Account "
                        + account.getAccountNumber()
                        + " was frozen by admin");

        return ResponseEntity.ok(
                "Account frozen successfully");
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/unfreeze/{accountId}")
    public ResponseEntity<String> unfreezeAccount(@PathVariable Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found"));

        account.setStatus(AccountStatus.ACTIVE);

        accountRepository.save(account);

        auditLogService.log(
                "ACCOUNT_UNFROZEN",
                account.getUser().getEmail(),
                "Account "
                        + account.getAccountNumber()
                        + " was unfrozen by admin");

        return ResponseEntity.ok(
                "Account activated successfully");
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/close/{accountId}")
    public ResponseEntity<String> closeAccount(@PathVariable Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found"));

        account.setStatus(AccountStatus.CLOSED);

        accountRepository.save(account);

        auditLogService.log(
                "ACCOUNT_CLOSED",
                account.getUser().getEmail(),
                "Account "
                        + account.getAccountNumber()
                        + " was closed by admin");

        return ResponseEntity.ok(
                "Account closed successfully");
    }
    
    
}
