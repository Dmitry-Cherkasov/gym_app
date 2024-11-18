package com.gym_app.core.services;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        // Set some mock values for testing
        jwtTokenProvider.setSecretKey("oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI");
        jwtTokenProvider.setExpiration(3600000); // 1 hour expiration
    }

    @Test
    void testGenerateToken() {
        String username = "user123";
        String token = jwtTokenProvider.generateToken(username);

        assertNotNull(token, "Token should not be null");
        assertTrue(jwtTokenProvider.getUsernameFromToken(token).equals(username), "Token should contain the username");
    }

    @Test
    void testGetUsernameFromToken() {
        String username = "user123";
        String token = jwtTokenProvider.generateToken(username);

        String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

        assertEquals(username, extractedUsername, "Extracted username should match the generated one");
    }

    @Test
    void testIsValidToken() {
        String username = "user123";
        String token = jwtTokenProvider.generateToken(username);

        assertTrue(jwtTokenProvider.isValid(token), "Token should be valid");
    }

    @Test
    void testInvalidateToken() {
        String username = "user123";
        String token = jwtTokenProvider.generateToken(username);

        jwtTokenProvider.invalidateToken(token);

        assertFalse(jwtTokenProvider.isValid(token), "Token should be invalid after invalidation");
    }

    @Test
    void testExpiredToken() {
        String username = "user123";
        jwtTokenProvider.setExpiration(-1000); // Set an expired token for testing
        String token = jwtTokenProvider.generateToken(username);

        // Simulate expiration handling
        assertFalse(jwtTokenProvider.isValid(token), "Token should be invalid because it's expired");
    }
}
