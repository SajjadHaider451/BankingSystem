package com.example.BankingSystem;

import com.example.BankingSystem.account.*;
import com.example.BankingSystem.audit.AuditLogService;
import com.example.BankingSystem.exception.UserNotFoundException;
import com.example.BankingSystem.user.UserRepository;
import com.example.BankingSystem.user.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_Success() {
        // Arrange
        Long userId = 1L;
        CreateAccountRequest request = new CreateAccountRequest("SAVINGS");
        
        Users user = new Users();
        user.setId(userId);
        user.setEmail("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        // Use answer to capture the account object passed to save, to ensure it's returned
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            account.setId(100L); // Set an ID for the saved account
            return account;
        });

        // Act
        AccountResponse response = accountService.createAccount(userId, request);

        // Assert
        assertNotNull(response);
        assertEquals("SAVINGS", response.getAccountType());
        assertEquals(userId, response.getUserId());
        
        verify(userRepository, times(1)).findById(userId);
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(auditLogService, times(1)).log(eq("ACCOUNT_CREATED"), eq("test@example.com"), anyString());
    }

    @Test
    void createAccount_UserNotFound() {
        // Arrange
        Long userId = 1L;
        CreateAccountRequest request = new CreateAccountRequest("SAVINGS");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> accountService.createAccount(userId, request));
        
        verify(userRepository, times(1)).findById(userId);
        verify(accountRepository, never()).save(any(Account.class));
        verify(auditLogService, never()).log(anyString(), anyString(), anyString());
    }
}
