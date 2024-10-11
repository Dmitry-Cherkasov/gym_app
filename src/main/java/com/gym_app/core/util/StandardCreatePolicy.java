package com.gym_app.core.util;

import org.springframework.stereotype.Component;


@Component
public class StandardCreatePolicy implements CreatePolicy{
    private final int PASSWORD_LENGTH = 10;
    private final int MIN_TRAINEE_AGE = 5;

    @Override
    public String getUserName(String firstName, String lastName) {
        return firstName+"."+lastName;
    }

    @Override
    public String getPassword(int length) {
        return PasswordCreator.createPassword(PASSWORD_LENGTH);
    }

}
