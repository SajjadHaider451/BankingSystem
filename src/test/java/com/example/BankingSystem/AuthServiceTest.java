package com.example.BankingSystem;

import com.example.BankingSystem.audit.AuditLogService;
import com.example.BankingSystem.auth.AuthResponse;
import com.example.BankingSystem.auth.AuthService;
import com.example.BankingSystem.auth.LoginRequest;
import com.example.BankingSystem.auth.RegisterRequest;
import com.example.BankingSystem.security.JwtService;
import com.example.BankingSystem.user.UserRepository;
import com.example.BankingSystem.user.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private Users user;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("test@example.com", "password123");
        registerRequest = new RegisterRequest("testuser", "test@example.com", "password123");
        user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void login_success() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user.getEmail())).thenReturn("mockToken");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mockToken", response.getToken());
        verify(auditLogService).log("LOGIN", user.getEmail(), "User login");
    }

    @Test
    void login_userNotFound() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        verify(auditLogService).log("LOGIN_FAILED", loginRequest.getEmail(), "Invalid login attempt");
    }

    @Test
    void login_invalidPassword() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void register_success() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("User registered successfully", response.getToken());
        
        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(userRepository).save(userCaptor.capture());
        
        Users savedUser = userCaptor.getValue();
        assertEquals(registerRequest.getEmail(), savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        
        verify(auditLogService).log("REGISTER", registerRequest.getEmail(), "User account registered");
    }

    @Test
    void register_emailAlreadyExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
    }
}
