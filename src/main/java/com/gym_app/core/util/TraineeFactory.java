package com.gym_app.core.util;

import com.gym_app.core.dto.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TraineeFactory extends UserFactory{

    @Autowired
    public TraineeFactory(CreatePolicy policy){
        super(policy);
    }


    public Trainee createTrainee(String firstName, String lastName, Boolean isActive, LocalDate date, String address) {
            String userName = super.getCreatePolicy().getUserName(firstName, lastName);
            String password = super.getCreatePolicy().getPassword(10);
            return new Trainee(firstName, lastName, userName, password,isActive, date, address);
    }
}
