package com.example.BankingSystem.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="transactions")
public class Transaction {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal amount;
	private String transactionType;
	private LocalDateTime timestamp;
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "sender_account_id")
	private Account senderAccount;

	@ManyToOne
	@JoinColumn(name = "receiver_account_id")
	private Account receiverAccount;

	public Long getId() {
		return id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setReceiverAccount(Account recieverAccount) {
		this.receiverAccount = recieverAccount;
	}
	
	public Account getReceiverAccount() {
		return receiverAccount;
	}
	
	public void setSenderAccount(Account senderAccount) {
		this.senderAccount = senderAccount;
	}
	
	public Account getSenderAccount() {
		return senderAccount;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", amount=" + amount + ", transactionType=" + transactionType + ", timestamp="
				+ timestamp + ", description=" + description + "]";
	}
	
	
	
}
