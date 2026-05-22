package com.example.BankingSystem.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * finds users, saves users, check duplicate emails
 */

/**
 * Repository interface for performing CRUD and query operations on the Users entity in the database.
 */

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);
}
