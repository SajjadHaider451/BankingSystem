package com.example.BankingSystem.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD and query operations on the Transaction entity in the database.
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderAccountIdOrReceiverAccountId(Long senderId, Long receiverId);

    List<Transaction> findBySenderAccountId(Long id);

    List<Transaction> findByReceiverAccountId(Long id);
}

