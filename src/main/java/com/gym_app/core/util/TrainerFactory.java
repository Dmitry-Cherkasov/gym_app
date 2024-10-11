package com.gym_app.core.util;

import com.gym_app.core.dto.Trainer;

import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.repo.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainerFactory extends UserFactory{
    @Autowired
    TrainerRepository trainerRepository;
    @Autowired
    public TrainerFactory(CreatePolicy policy) {
        super(policy);
    }

    public Trainer createTrainer(String firstName, String lastName, Boolean isActive, TrainingType specialization) {
        String baseUserName = super.getCreatePolicy().getUserName(firstName, lastName);
        String userName = baseUserName;
        int serialNumber = 1;

        while (trainerRepository.getRepository().containsKey(userName)) {
            userName = baseUserName + serialNumber;  // Reset to base username with new serial number
            serialNumber++;
        }
        String password = super.getCreatePolicy().getPassword(10);

        return new Trainer(firstName, lastName, userName, password,isActive, specialization);
    }

}
