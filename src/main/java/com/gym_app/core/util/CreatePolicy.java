package com.gym_app.core.util;

import java.time.LocalDate;

public interface CreatePolicy {
    String getUserName(String firstName, String lastName);
    String getPassword(int length);
}
