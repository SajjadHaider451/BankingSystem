package com.example.BankingSystem.account;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * REST controller for handling bank account management operations, such as creating accounts and retrieving account information.
 * Receives request from user and forwards to accountservice.
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {

    /*
     * =========================================
     * SERVICE LAYER
     * =========================================
     *
     * The controller should NOT contain
     * business logic.
     *
     * It delegates work to AccountService.
     */
    private final AccountService accountService;

    /*
     * =========================================
     * CONSTRUCTOR INJECTION
     * =========================================
     */
    public AccountController(AccountService accountService) {

        this.accountService = accountService;
    }

    /*
     * =========================================
     * CREATE ACCOUNT
     * =========================================
     *
     * Endpoint:
     * POST /accounts?userId=1
     *
     * Example JSON:
     *
     * {
     *   "accountType": "CHECKING"
     * }
     */
    
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
    		@RequestParam Long userId, 
    		@Valid @RequestBody CreateAccountRequest request) {

        /*
         * Call service layer
         */
        AccountResponse response = accountService.createAccount(userId, request);

        /*
         * Return HTTP 201 Created
         */
        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    /*
     * =========================================
     * GET ACCOUNT BY ID
     * =========================================
     *
     * Endpoint:
     * GET /accounts/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {

        /*
         * Retrieve account
         */
        AccountResponse response = accountService.getAccountById(id);

        /*
         * Return HTTP 200 OK
         */
        return ResponseEntity.ok(response);
    }

    /*
     * =========================================
     * GET ALL ACCOUNTS FOR USER
     * =========================================
     *
     * Endpoint:
     * GET /accounts/user/1
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByUser(@PathVariable Long userId) {

        /*
         * Retrieve user accounts
         */
        List<AccountResponse> response = accountService.getAccountsByUser(userId);

        /*
         * Return HTTP 200 OK
         */
        return ResponseEntity.ok(response);
    }
	
}
