package com.example.BankingSystem.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BankingSystem.Entity.Transaction;

/*
 * store transaction history, fetch transfers, fetch withdrawals/deposits
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderAccountId(Long id);

    List<Transaction> findByReceiverAccountId(Long id);
}

