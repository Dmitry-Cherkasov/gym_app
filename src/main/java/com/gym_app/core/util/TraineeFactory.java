
package com.gym_app.core.util;

import com.gym_app.core.dto.Trainee;
import com.gym_app.core.dto.User;
import com.gym_app.core.dto.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TraineeFactory implements UserFactory{
    private UserDataFactory userDataFactory;

    @Autowired
    public TraineeFactory(UserDataFactory userDataFactory){
        this.userDataFactory = userDataFactory;
    }

    @Override
    public User createUser(String firstName, String lastName, boolean isActive) {
        UserData userData = userDataFactory.createUserData(firstName,lastName,isActive);
        return new Trainee(userData, null, null);
    }

    public Trainee createTrainee(String firstName, String lastName, Boolean isActive, LocalDate date, String address) {
        UserData userData = userDataFactory.createUserData(firstName,lastName,isActive);
        return new Trainee (userData, date, address);
    }
}
