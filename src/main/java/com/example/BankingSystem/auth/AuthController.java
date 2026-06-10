package com.example.BankingSystem.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.BankingSystem.audit.AuditLogService;
import com.example.BankingSystem.user.Users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for handling user authentication requests, including registration and login.
 */
@Tag(
        name = "Authentication",
        description = "User registration and login"
)
@RestController // handles API requests
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    

    public AuthController(AuthService authService) {

        this.authService = authService;
        
    }

    /*
     * =========================================
     * REGISTER USER
     * =========================================
     */
    
    @Operation(
            summary = "Register a new user",
            description = "Creates a new banking customer account"
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        
      
        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
        // @RequestBody converts json into dto objects
    }

    /*
     * =========================================
     * LOGIN USER
     * =========================================
     */
    @Operation(
            summary = "Authenticate user",
            description = "Logs in a user and returns JWT token"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);
       
        return ResponseEntity.ok(response);
    }
    
}