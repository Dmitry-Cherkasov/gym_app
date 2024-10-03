package com.gym_app.core.util;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordCreator {
    private static final String ORIGIN = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String createPassword(int length) {
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(ORIGIN.length());
            password.append(ORIGIN.charAt(randomIndex));
        }
        return password.toString();

    }
}
