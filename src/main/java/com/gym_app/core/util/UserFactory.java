package com.gym_app.core.util;

import com.gym_app.core.dto.User;

public interface UserFactory <T extends User> {


    public T createUser(String firstName, String lastName, boolean isActive, Object... extraArg);

}
