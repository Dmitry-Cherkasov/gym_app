package com.gym_app.core.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginProtectorTest {

    private LoginProtector loginProtector;

    @BeforeEach
    void setUp() {
        loginProtector = new LoginProtector();
        loginProtector.setLockoutTime(1000); // Set lockout time to 1 second for testing
    }

    @Test
    void testLoginSucceeded_RemovesFromCache() {
        // Arrange
        String username = "testUser";
        loginProtector.loginFailed(username); // Add a failed attempt
        loginProtector.loginFailed(username);

        // Act
        loginProtector.loginSucceeded(username);

        // Assert
        assertFalse(loginProtector.isBlocked(username));
    }

    @Test
    void testLoginFailed_IncrementsAttempts() {
        // Arrange
        String username = "testUser";

        // Act
        loginProtector.loginFailed(username);
        loginProtector.loginFailed(username);

        // Assert
        assertFalse(loginProtector.isBlocked(username));
        loginProtector.loginFailed(username);
        assertTrue(loginProtector.isBlocked(username));
    }

    @Test
    void testIsBlocked_ReturnsTrueWhenBlocked() {
        // Arrange
        String username = "testUser";
        for (int i = 0; i < 3; i++) {
            loginProtector.loginFailed(username);
        }

        // Act
        boolean isBlocked = loginProtector.isBlocked(username);

        // Assert
        assertTrue(isBlocked);
    }

    @Test
    void testIsBlocked_ReturnsFalseAfterLockoutExpires() throws InterruptedException {
        // Arrange
        String username = "testUser";
        for (int i = 0; i < 3; i++) {
            loginProtector.loginFailed(username);
        }

        // Act
        Thread.sleep(1500); // Wait for lockout time to expire
        boolean isBlocked = loginProtector.isBlocked(username);

        // Assert
        assertFalse(isBlocked);
    }

    @Test
    void testGetAndSetLockoutTime() {
        // Act
        loginProtector.setLockoutTime(2000);
        int lockoutTime = loginProtector.getLockoutTime();

        // Assert
        assertEquals(2000, lockoutTime);
    }

    @Test
    void testMultipleUsers_IndependentLockouts() {
        // Arrange
        String user1 = "user1";
        String user2 = "user2";

        // Act
        loginProtector.loginFailed(user1);
        loginProtector.loginFailed(user1);
        loginProtector.loginFailed(user1);

        loginProtector.loginFailed(user2);

        // Assert
        assertTrue(loginProtector.isBlocked(user1));
        assertFalse(loginProtector.isBlocked(user2));
    }
}
