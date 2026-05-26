package com.example.BankingSystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/*
 * create JWT tokens, validate JWT tokens, extract email from token
 */

@Service
public class JwtService {

    /*
     * Secret key for signing tokens
     */
    private static final String SECRET_KEY =
            "mySuperSecretKeymySuperSecretKey12345";

    /*
     * Generate signing key
     */
    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes());
    }

    /*
     * Generate JWT token
     */
    public String generateToken(String email) {

        return Jwts.builder()

                .setSubject(email)

                .setIssuedAt(new Date())

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60 * 24))

                .signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256)

                .compact();
    }

    /*
     * Extract email from token
     */
    public String extractEmail(String token) {

        return extractClaim(
                token,
                Claims::getSubject);
    }

    /*
     * Extract expiration date
     */
    public Date extractExpiration(String token) {

        return extractClaim(
                token,
                Claims::getExpiration);
    }

    /*
     * Generic claim extractor
     */
    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    /*
     * Extract all claims
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()

                .setSigningKey(getSigningKey())

                .build()

                .parseClaimsJws(token)

                .getBody();
    }

    /*
     * Check if token expired
     */
    private boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }

    /*
     * Validate token
     */
    public boolean isTokenValid(
            String token,
            String email) {

        String extractedEmail =
                extractEmail(token);

        return extractedEmail.equals(email)
                && !isTokenExpired(token);
    }
}
