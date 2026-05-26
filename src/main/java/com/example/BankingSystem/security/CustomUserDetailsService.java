package com.example.BankingSystem.security;

import com.example.BankingSystem.user.*;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

/*
 * load users from database, connect Spring Security to your Users entity
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users user = userRepository.findByEmail(email)
        		.orElseThrow(() ->
                      new UsernameNotFoundException(
                            "User not found"));

        return User.builder()

                .username(user.getEmail())

                .password(user.getPassword())

                .roles("ROLE_USERS")

                .build();
    }
}
