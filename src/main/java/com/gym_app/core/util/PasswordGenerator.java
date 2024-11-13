package com.gym_app.core.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String ORIGIN = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public static String createPassword(int length) {
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(ORIGIN.length());
            password.append(ORIGIN.charAt(randomIndex));
        }
        return password.toString();

    }

    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}

