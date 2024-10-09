package com.gym_app.core.util;

import com.gym_app.core.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class UserFactory {
    private CreatePolicy createPolicy;

    @Autowired
    public UserFactory(CreatePolicy policy){
        this.createPolicy = policy;
    }

    public CreatePolicy getCreatePolicy() {
        return createPolicy;
    }

    public void setCreatePolicy(CreatePolicy createPolicy) {
        this.createPolicy = createPolicy;
    }
}
