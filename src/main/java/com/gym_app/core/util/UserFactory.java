package com.gym_app.core.util;

import com.gym_app.core.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    private CreatePolicy createPolicy;

    @Autowired
    public UserFactory(CreatePolicy policy){
        this.createPolicy = policy;
    }

    public UserFactory() {
    }

    public User createUser(String firstName, String lastName, boolean isActive){
        String userName = createPolicy.getUserName(firstName, lastName);
        String password = createPolicy.getPassword(10);
        return new User(firstName, lastName, userName, password, isActive);

    }

    public CreatePolicy getCreatePolicy() {
        return createPolicy;
    }

    public void setCreatePolicy(CreatePolicy createPolicy) {
        this.createPolicy = createPolicy;
    }
}
