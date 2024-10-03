package com.gym_app.core.util;

public interface CreatePolicy {
    String getUserName(String firstName, String lastName);
    String getPassword(int length);
}
