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
@Table(name = "accounts")
public class Account {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int accountNumber;
	private BigDecimal balance;
	private String accountType;
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;
	
	public Long getId() {
		return id;
	}
	public int getAccountNumber() {
		return accountNumber;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public String getAccountType() {
		return accountType;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
		return "Account [id=" + id + ", accountNumber=" + accountNumber + ", balance=" + balance + ", accountType="
				+ accountType + ", createdAt=" + createdAt + "]";
	}
	
	
}
