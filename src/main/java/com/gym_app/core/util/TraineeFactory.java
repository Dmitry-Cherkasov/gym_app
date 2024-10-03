package com.gym_app.core.util;

import com.gym_app.core.dto.Trainee;
import com.gym_app.core.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TraineeFactory implements UserFactory{
   private CreatePolicy createPolicy;

    @Autowired
    public TraineeFactory(CreatePolicy policy){
        this.createPolicy = policy;
    }

    @Override
    public User createUser(String firstName, String lastName, String userName, String password, boolean isActive) {

        return new Trainee(firstName, lastName, userName, password, isActive, null, null);
    }

    public Trainee createTrainee(String firstName, String lastName, Boolean isActive, LocalDate date, String address) {
        String userName = createPolicy.getUserName(firstName, lastName);
        String password = createPolicy.getPassword(10);

        return new Trainee (firstName, lastName, userName, password, isActive, date, address);
    }
}
