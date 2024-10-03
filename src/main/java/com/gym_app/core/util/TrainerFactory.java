package com.gym_app.core.util;

import com.gym_app.core.dto.Trainer;
import com.gym_app.core.dto.User;
import com.gym_app.core.enums.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainerFactory implements UserFactory{
   private CreatePolicy createPolicy;

    @Autowired
    public TrainerFactory(CreatePolicy policy){
        this.createPolicy = policy;
    }

    public Trainer createTrainer(String firstName, String lastName, Boolean isActive, TrainingType specialization) {
        String userName = createPolicy.getUserName(firstName, lastName);
        String password = createPolicy.getPassword(10);

        return new Trainer(firstName, lastName, userName, password, isActive, specialization);
    }

    @Override
    public User createUser(String firstName, String lastName, String userName, String password, boolean isActive) {

        return new Trainer(firstName, lastName, userName, password, isActive, null);
    }
}
