package com.gym_app.core.util;

import com.gym_app.core.dto.Trainer;

import com.gym_app.core.enums.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainerFactory extends UserFactory{

    @Autowired
    public TrainerFactory(CreatePolicy policy) {
        super(policy);
    }

    public Trainer createTrainer(String firstName, String lastName, Boolean isActive, TrainingType specialization) {
        String userName = super.getCreatePolicy().getUserName(firstName, lastName);
        String password = super.getCreatePolicy().getPassword(10);

        return new Trainer(firstName, lastName, userName, password,isActive, specialization);
    }

}
