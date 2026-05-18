package com.example.BankingSystem.auth.service;

import com.example.BankingSystem.Entity.Users;
import com.example.BankingSystem.dto.auth.AuthResponse;
import com.example.BankingSystem.dto.auth.LoginRequest;
import com.example.BankingSystem.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	
	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public AuthResponse login(LoginRequest request) {

	    /*
	     * Find user by email
	     */
	    Users user = userRepository.findByEmail(request.getEmail())
	            .orElseThrow(() ->
	                    new RuntimeException(
	                            "Invalid email or password"));

	    /*
	     * Check password
	     */
	    boolean passwordMatches = passwordEncoder.matches(
	            request.getPassword(),
	            user.getPassword()
	    );

	    /*
	     * Validate password
	     */
	    if (!passwordMatches) {

	        throw new RuntimeException(
	                "Invalid email or password");
	    }

	    /*
	     * Login successful
	     */
	    return new AuthResponse("Login successful");
	}
}
