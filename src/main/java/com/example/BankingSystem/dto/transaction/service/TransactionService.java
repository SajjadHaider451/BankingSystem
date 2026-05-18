package com.example.BankingSystem.dto.transaction.service;


import com.example.BankingSystem.Entity.Account;
import com.example.BankingSystem.Entity.Transaction;
import com.example.BankingSystem.dto.transaction.DepositRequest;
import com.example.BankingSystem.dto.transaction.TransactionResponse;
import com.example.BankingSystem.dto.transaction.TransferRequest;
import com.example.BankingSystem.dto.transaction.WithdrawRequest;
import com.example.BankingSystem.exeception.AccountNotFoundException;
import com.example.BankingSystem.exeception.InsufficientFundsException;
import com.example.BankingSystem.repo.*;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
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

        /*
         * Return response DTO
         */
        return mapToResponse(savedTransaction);
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
}
