package com.example.BankingSystem.transaction;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.BankingSystem.account.Account;
import com.example.BankingSystem.account.AccountRepository;
import com.example.BankingSystem.account.AccountService;
import com.example.BankingSystem.account.AccountStatus;
import com.example.BankingSystem.audit.AuditLogService;
import com.example.BankingSystem.exception.AccountFrozenException;
import com.example.BankingSystem.exception.AccountNotFoundException;
import com.example.BankingSystem.exception.InsufficientFundsException;

import jakarta.transaction.Transactional;

/**
 * Service class containing the business logic for financial transactions, including deposits, withdrawals, and transfers between bank accounts.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AuditLogService auditLogService;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository, 
                              AccountService accountService,
                              AuditLogService auditLogService) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.auditLogService = auditLogService;
    }

    /*
     * =========================================
     * DEPOSIT MONEY
     * =========================================
     */
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {

        /*
         * Validate deposit amount
         * Amount must be greater than 0
         */
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Deposit amount must be greater than zero");
        }
        
        

        /*
         * Find account in database
         */
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));
        
        
        accountService.validateAccountOwnership(account);
        
        /*
         * Add money to balance
         */
        account.setBalance(account.getBalance().add(request.getAmount()));

        /*
         * Save updated account
         */
        accountRepository.save(account);

        /*
         * Create transaction record
         */
        Transaction transaction = new Transaction();

        transaction.setAmount(request.getAmount());
        transaction.setTransactionType("DEPOSIT");
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription("Deposit into account");

        /*
         * Deposit only needs receiver account
         */
        transaction.setReceiverAccount(account);

        /*
         * Save transaction
         */
        Transaction savedTransaction =
                transactionRepository.save(transaction);
        
        auditLogService.log(
                "DEPOSIT",
                account.getUser().getEmail(),
                "Deposited $"
                        + request.getAmount()
                        + " into account "
                        + account.getAccountNumber()
        );
        
        /*
         * Return DTO response
         */
        return mapToResponse(savedTransaction);
    }

    /*
     * =========================================
     * WITHDRAW MONEY
     * =========================================
     */
    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {

        /*
         * Validate amount
         */
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Withdraw amount must be greater than zero");
        }

        /*
         * Find account
         */
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));
        
        accountService.validateAccountOwnership(account);
        validateAccountStatus(account);
        
        /*
         * Check sufficient funds
         */
        if (account.getBalance().compareTo(request.getAmount()) < 0) {

            throw new InsufficientFundsException(
                    "Insufficient funds");
        }

        /*
         * Subtract money from balance
         */
        account.setBalance(account.getBalance().subtract(request.getAmount()));

        /*
         * Save updated account
         */
        accountRepository.save(account);

        /*
         * Create transaction record
         */
        Transaction transaction = new Transaction();

        transaction.setAmount(request.getAmount());
        transaction.setTransactionType("WITHDRAW");
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription("Withdraw from account");

        /*
         * Withdraw uses sender account
         */
        transaction.setSenderAccount(account);

        /*
         * Save transaction
         */
        Transaction savedTransaction =
                transactionRepository.save(transaction);
        
        auditLogService.log(
                "WITHDRAW",
                account.getUser().getEmail(),
                "Withdrew $"
                        + request.getAmount()
                        + " from account "
                        + account.getAccountNumber()
        );
        
        /*
         * Return DTO response
         */
        return mapToResponse(savedTransaction);
    }

    /*
     * =========================================
     * TRANSFER MONEY
     * =========================================
     */
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {

        /*
         * Validate transfer amount
         */
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Transfer amount must be greater than zero");
        }

        /*
         * Find sender account
         */
        Account sender = accountRepository
                .findById(request.getSenderAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Sender account not found"));
        
        accountService.validateAccountOwnership(sender);
        validateAccountStatus(sender);
        
        

        /*
         * Find receiver account
         */
        Account receiver = accountRepository
                .findById(request.getReceiverAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Receiver account not found"));

        /*
         * Check sender balance
         */
        if (sender.getBalance().compareTo(request.getAmount()) < 0) {

            throw new InsufficientFundsException(
                    "Insufficient funds");
        }

        /*
         * Remove money from sender
         */
        sender.setBalance(
                sender.getBalance().subtract(request.getAmount()));

        /*
         * Add money to receiver
         */
        receiver.setBalance(
                receiver.getBalance().add(request.getAmount()));

        /*
         * Save BOTH accounts
         */
        accountRepository.save(sender);
        accountRepository.save(receiver);

        /*
         * Create transaction record
         */
        Transaction transaction = new Transaction();

        transaction.setAmount(request.getAmount());
        transaction.setTransactionType("TRANSFER");
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription("Transfer between accounts");

        transaction.setSenderAccount(sender);
        transaction.setReceiverAccount(receiver);

        /*
         * Save transaction
         */
        Transaction savedTransaction =
                transactionRepository.save(transaction);
        
        auditLogService.log(
                "TRANSFER",
                sender.getUser().getEmail(),
                "Transferred $"
                        + request.getAmount()
                        + " from account "
                        + sender.getAccountNumber()
                        + " to account "
                        + receiver.getAccountNumber()
        );
        
        /*
         * Return response DTO
         */
        return mapToResponse(savedTransaction);
    }

    /*
     * =========================================
     * GET ACCOUNT TRANSACTION HISTORY
     * =========================================
     */
    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {

        /*
         * Verify account exists
         */
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("Account not found");
        }

        /*
         * Retrieve transactions
         */
        return transactionRepository
                .findBySenderAccountIdOrReceiverAccountId(accountId, accountId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /*
     * =========================================
     * MAP ENTITY -> DTO
     * =========================================
     */
    private TransactionResponse mapToResponse(Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTimestamp(),
                transaction.getDescription(),

                /*
                 * If sender account exists,
                 * return sender account id
                 */
                transaction.getSenderAccount() != null
                        ? transaction.getSenderAccount().getId()
                        : null,

                /*
                 * If receiver account exists,
                 * return receiver account id
                 */
                transaction.getReceiverAccount() != null
                        ? transaction.getReceiverAccount().getId()
                        : null
        );
    }
    
    private void validateAccountStatus(
            Account account) {

        if (account.getStatus()
                == AccountStatus.FROZEN) {

            throw new AccountFrozenException(
                    "Account is frozen");
        }

        if (account.getStatus()
                == AccountStatus.CLOSED) {

            throw new AccountFrozenException(
                    "Account is closed");
        }
    }
}
