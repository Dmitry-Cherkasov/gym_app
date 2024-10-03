package com.gym_app.core.util;

public class StandardCreatePolicy implements CreatePolicy{

    @Override
    public String getUserName(String firstName, String lastName) {
        return firstName+"."+lastName;
    }

    @Override
    public String getPassword(int length) {
        return PasswordCreator.createPassword(10);
    }
}
