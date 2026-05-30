package com.example.BankingSystem.transaction;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Transactions",
        description = "Deposit, withdrawal, and transfer operations"
)
@RestController
@RequestMapping("/transactions")
public class TransactionController {
	
	/*
     * =========================================
     * SERVICE LAYER
     * =========================================
     *
     * The controller delegates business logic
     * to the TransactionService.
     */
	private final TransactionService transService;
	
	/*
     * =========================================
     * CONSTRUCTOR INJECTION
     * =========================================
     */
	public TransactionController(TransactionService transService) {
		this.transService = transService;
	}
	
	/*
     * =========================================
     * DEPOSIT MONEY
     * =========================================
     *
     * Endpoint:
     * POST /transactions/deposit
     *
     * Example JSON:
     *
     * {
     *   "accountId": 1,
     *   "amount": 500.00,
     *   "description": "Paycheck deposit"
     * }
     */
	@PostMapping("/deposit")
	public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request){
		
		/*
         * Call service layer
         */
		TransactionResponse response = transService.deposit(request);
		
		/*
         * Return HTTP 200 OK
         */
        return ResponseEntity.ok(response);
		
	}
	
	/*
     * =========================================
     * WITHDRAW MONEY
     * =========================================
     *
     * Endpoint:
     * POST /transactions/withdraw
     *
     * Example JSON:
     *
     * {
     *   "accountId": 1,
     *   "amount": 100.00,
     *   "description": "ATM withdrawal"
     * }
     */
	
	@PostMapping("/withdraw")
	public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request){
		
		/*
         * Call service layer
         */
		TransactionResponse response = transService.withdraw(request);
		
		/*
         * Return HTTP 200 OK
         */
        return ResponseEntity.ok(response);
	}
	
	/*
     * =========================================
     * TRANSFER MONEY
     * =========================================
     *
     * Endpoint:
     * POST /transactions/transfer
     *
     * Example JSON:
     *
     * {
     *   "senderAccountId": 1,
     *   "receiverAccountId": 2,
     *   "amount": 250.00,
     *   "description": "Rent payment"
     * }
     */
	@PostMapping("/transfer")
	public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request){
		
		/*
         * Call service layer
         */
		TransactionResponse response = transService.transfer(request);
		
		/*
         * Return HTTP 200 OK
         */
        return ResponseEntity.ok(response);
	}
	
	/*
     * =========================================
     * GET ACCOUNT TRANSACTION HISTORY
     * =========================================
     *
     * Endpoint:
     * GET /transactions/account/1
     */
	@GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>>
    getTransactionsByAccountId(

            @PathVariable Long accountId) {

        /*
         * Retrieve transaction history
         */
        List<TransactionResponse> response =
                transService.getTransactionsByAccountId(accountId);

        /*
         * Return HTTP 200 OK
         */
        return ResponseEntity.ok(response);
    }
	
}
