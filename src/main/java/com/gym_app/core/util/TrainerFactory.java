package com.gym_app.core.util;

import com.gym_app.core.dto.Trainer;
import com.gym_app.core.dto.User;
import com.gym_app.core.dto.UserData;
import com.gym_app.core.enums.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainerFactory implements UserFactory{
   private UserDataFactory userDataFactory;

    @Autowired
    public TrainerFactory(UserDataFactory userDataFactory){
        this.userDataFactory = userDataFactory;
    }

    public Trainer createTrainer(String firstName, String lastName, Boolean isActive, TrainingType specialization) {
        UserData userData = userDataFactory.createUserData(firstName,lastName, isActive);
        return new Trainer(userData, specialization);
    }

    @Override
    public User createUser(String firstName, String lastName, boolean isActive) {
        UserData userData = userDataFactory.createUserData(firstName,lastName, isActive);
        return new Trainer(userData, null);
    }
}
