package com.example.BankingSystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the application, defining authentication and authorization rules, and password encoding.
 */
@Configuration
public class SecurityConfig {

    /*
     * Password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    /*
     * Security configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

                /*
                 * Disable CSRF for APIs/Postman testing
                 */
                .csrf(csrf -> csrf.disable())

                /*
                 * Configure endpoint permissions
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * Allow auth endpoints WITHOUT login
                         */
                        .requestMatchers("/auth/**").permitAll()

                        /*
                         * Everything else requires authentication
                         */
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

