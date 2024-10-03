package com.gym_app.core.util;

import com.gym_app.core.dto.User;

public interface UserFactory {

    User createUser(String firstName, String lastName, String userName, String password, boolean isActive);
    
}
