package com.example.BankingSystem.auth;

import com.example.BankingSystem.user.Users;
import com.example.BankingSystem.user.UserRepository;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class containing the business logic for user authentication, including user registration and login validation.
 */
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
	
	public AuthResponse register(RegisterRequest request) {

	    /*
	     * Check if email already exists
	     */
	    if (userRepository.existsByEmail(request.getEmail())) {

	        throw new RuntimeException(
	                "Email already exists");
	    }

	    /*
	     * Create new user
	     */
	    Users user = new Users();

	    user.setUsername(request.getUsername());

	    user.setEmail(request.getEmail());

	    /*
	     * VERY IMPORTANT
	     * Encrypt password before saving
	     */
	    user.setPassword(
	            passwordEncoder.encode(
	                    request.getPassword()));

	    /*
	     * Default role
	     */
	    user.setRole("CUSTOMER");

	    /*
	     * Set creation timestamp
	     */
	    user.setCreatedAt(LocalDateTime.now());

	    /*
	     * Save user to database
	     */
	    userRepository.save(user);

	    /*
	     * Return response
	     */
	    return new AuthResponse(
	            "User registered successfully");
	}

	
}
