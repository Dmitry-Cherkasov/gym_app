package com.gym_app.core.util;

import com.gym_app.core.dto.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDataFactory{
   private CreatePolicy createPolicy;

    @Autowired
    public UserDataFactory(CreatePolicy policy){
        this.createPolicy = policy;
    }

    public UserData createUserData(String firstName, String lastName, boolean isActive) {
        String userName = createPolicy.getUserName(firstName, lastName);
        String password = createPolicy.getPassword(10);

        return new UserData(firstName, lastName, userName, password, isActive);
    }

    public UserData createUserData(String firstName, String lastName, String userName, String password, boolean isActive) {
        return new UserData(firstName, lastName, userName, password, isActive);
    }

}
