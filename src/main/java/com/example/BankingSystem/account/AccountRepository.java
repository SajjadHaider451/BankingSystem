package com.example.BankingSystem.account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD and query operations on the Account entity in the database.
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(int accountNumber);

    List<Account> findByUserId(Long userId);
}
