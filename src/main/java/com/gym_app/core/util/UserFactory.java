package com.gym_app.core.util;

import com.gym_app.core.dto.common.User;

public interface UserFactory <T extends User> {


    T createUser(String firstName, String lastName, boolean isActive, Object... extraArg);

}
