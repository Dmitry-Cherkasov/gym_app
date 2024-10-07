package com.gym_app.core.util;

import com.gym_app.core.dto.User;
import com.gym_app.core.dto.UserData;

public interface UserFactory {

    User createUser(String firstName, String lastName, boolean isActive);
    
}
