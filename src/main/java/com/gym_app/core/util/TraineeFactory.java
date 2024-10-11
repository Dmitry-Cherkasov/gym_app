package com.gym_app.core.util;

import com.gym_app.core.dto.Trainee;
import com.gym_app.core.repo.TraineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TraineeFactory extends UserFactory {
    @Autowired
    TraineeRepository traineeRepository;

    @Autowired
    public TraineeFactory(CreatePolicy policy) {
        super(policy);
    }


    public Trainee createTrainee(String firstName, String lastName, Boolean isActive, LocalDate date, String address) {
        String baseUserName = super.getCreatePolicy().getUserName(firstName, lastName);
        String userName = baseUserName;
        int serialNumber = 1;

        while (traineeRepository.getRepository().containsKey(userName)) {
            userName = baseUserName + serialNumber;  // Reset to base username with new serial number
            serialNumber++;
        }
        String password = super.getCreatePolicy().getPassword(10);
        return new Trainee(firstName, lastName, userName, password, isActive, date, address);
    }
}
