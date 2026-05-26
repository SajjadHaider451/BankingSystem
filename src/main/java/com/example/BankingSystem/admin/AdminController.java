package com.example.BankingSystem.admin;


import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankingSystem.user.UserRepository;
import com.example.BankingSystem.user.Users;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * ONLY ADMINS CAN ACCESS
     */
    // ONLY authenticated admins can access this endpoint
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}
