package com.example.BankingSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.BankingSystem.account.Account;
import com.example.BankingSystem.account.AccountRepository;
import com.example.BankingSystem.account.AccountService;
import com.example.BankingSystem.account.AccountStatus;
import com.example.BankingSystem.audit.AuditLogService;
import com.example.BankingSystem.transaction.DepositRequest;
import com.example.BankingSystem.transaction.Transaction;
import com.example.BankingSystem.transaction.TransactionRepository;
import com.example.BankingSystem.transaction.TransactionService;
import com.example.BankingSystem.transaction.TransferRequest;
import com.example.BankingSystem.transaction.WithdrawRequest;
import com.example.BankingSystem.user.Users;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
	
	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private AccountService accountService;

	@Mock
	private AuditLogService auditLogService;
	
	@InjectMocks
	private TransactionService transactionService;
	
	@Test
	void depositShouldIncreaseBalance() {
	    Users user = new Users();
	    user.setEmail("test@example.com");

	    Account account = new Account();
	    account.setId(1L);
	    account.setAccountNumber(12345);
	    account.setUser(user);
	    account.setBalance(BigDecimal.valueOf(100));

	    DepositRequest request = new DepositRequest();
	    request.setAccountId(1L);
	    request.setAmount(BigDecimal.valueOf(50));

	    when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
	    when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

	    transactionService.deposit(request);

	    assertEquals(BigDecimal.valueOf(150), account.getBalance());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(any(Transaction.class));
        verify(auditLogService).log(any(), any(), any());
	}
	
	@Test
    void depositShouldThrowExceptionForInvalidAmount() {
        DepositRequest request = new DepositRequest();
        request.setAmount(BigDecimal.valueOf(-10));
        
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.deposit(request);
        });
    }

    @Test
    void depositShouldThrowAccountNotFoundException() {
        DepositRequest request = new DepositRequest();
        request.setAccountId(1L);
        request.setAmount(BigDecimal.valueOf(50));
        
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        
        org.junit.jupiter.api.Assertions.assertThrows(com.example.BankingSystem.exception.AccountNotFoundException.class, () -> {
            transactionService.deposit(request);
        });
    }
	
	@Test
	void withdrawShouldDecreaseBalance() {
		Users user = new Users();
	    user.setEmail("test@example.com");

	    Account account = new Account();
	    account.setId(1L);
	    account.setAccountNumber(12345);
	    account.setUser(user);
	    account.setBalance(BigDecimal.valueOf(100));
        account.setStatus(AccountStatus.ACTIVE);
	    
	    WithdrawRequest req = new WithdrawRequest();
	    req.setAccountId(1L);
	    req.setAmount(BigDecimal.valueOf(50));
	    
	    when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
	    when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

	    transactionService.withdraw(req);
	    
	    assertEquals(BigDecimal.valueOf(50), account.getBalance());
	    verify(accountRepository).save(account);
        verify(transactionRepository).save(any(Transaction.class));
        verify(auditLogService).log(any(), any(), any());
	}

    @Test
    void withdrawShouldThrowInsufficientFundsException() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(20));
        account.setStatus(AccountStatus.ACTIVE);
        
        WithdrawRequest req = new WithdrawRequest();
        req.setAccountId(1L);
        req.setAmount(BigDecimal.valueOf(50));
        
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        
        org.junit.jupiter.api.Assertions.assertThrows(com.example.BankingSystem.exception.InsufficientFundsException.class, () -> {
            transactionService.withdraw(req);
        });
    }

    @Test
    void withdrawShouldThrowAccountFrozenException() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100));
        account.setStatus(AccountStatus.FROZEN);
        
        WithdrawRequest req = new WithdrawRequest();
        req.setAccountId(1L);
        req.setAmount(BigDecimal.valueOf(50));
        
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        
        org.junit.jupiter.api.Assertions.assertThrows(com.example.BankingSystem.exception.AccountFrozenException.class, () -> {
            transactionService.withdraw(req);
        });
    }
    
    @Test
    void transferShouldTransferMoneyBetweenAccounts() {
        Users user = new Users();
        user.setEmail("test@example.com");

        Account sender = new Account();
        sender.setId(1L);
        sender.setAccountNumber(111);
        sender.setUser(user);
        sender.setBalance(BigDecimal.valueOf(100));
        sender.setStatus(AccountStatus.ACTIVE);

        Account receiver = new Account();
        receiver.setId(2L);
        receiver.setAccountNumber(222);
        receiver.setBalance(BigDecimal.valueOf(50));

        TransferRequest request = new TransferRequest(1L, 2L, BigDecimal.valueOf(30));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        transactionService.transfer(request);

        assertEquals(BigDecimal.valueOf(70), sender.getBalance());
        assertEquals(BigDecimal.valueOf(80), receiver.getBalance());
        verify(accountRepository, times(1)).save(sender);
        verify(accountRepository, times(1)).save(receiver);
        verify(transactionRepository).save(any(Transaction.class));
        verify(auditLogService).log(any(), any(), any());
    }

    @Test
    void transferShouldThrowInsufficientFundsException() {
        Account sender = new Account();
        sender.setId(1L);
        sender.setBalance(BigDecimal.valueOf(10));
        sender.setStatus(AccountStatus.ACTIVE);

        Account receiver = new Account();
        receiver.setId(2L);

        TransferRequest request = new TransferRequest(1L, 2L, BigDecimal.valueOf(30));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(receiver));

        org.junit.jupiter.api.Assertions.assertThrows(com.example.BankingSystem.exception.InsufficientFundsException.class, () -> {
            transactionService.transfer(request);
        });
    }

    @Test
    void transferShouldThrowAccountFrozenException() {
        Account sender = new Account();
        sender.setId(1L);
        sender.setBalance(BigDecimal.valueOf(100));
        sender.setStatus(AccountStatus.FROZEN);

        TransferRequest request = new TransferRequest(1L, 2L, BigDecimal.valueOf(30));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sender));
        
        org.junit.jupiter.api.Assertions.assertThrows(com.example.BankingSystem.exception.AccountFrozenException.class, () -> {
            transactionService.transfer(request);
        });
    }
}
