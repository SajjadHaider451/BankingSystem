package com.example.BankingSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
import com.example.BankingSystem.audit.AuditLogService;
import com.example.BankingSystem.transaction.DepositRequest;
import com.example.BankingSystem.transaction.Transaction;
import com.example.BankingSystem.transaction.TransactionRepository;
import com.example.BankingSystem.transaction.TransactionService;
import com.example.BankingSystem.user.Users;
import org.mockito.ArgumentMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
	
	// Mocks are fake implementation. 
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
	    
	    // Need to return a non-null transaction for mapToResponse
	    when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

	    transactionService.deposit(request);

	    assertEquals(BigDecimal.valueOf(150), account.getBalance());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(any(Transaction.class));
        verify(auditLogService).log(any(), any(), any());
	}
	
}
