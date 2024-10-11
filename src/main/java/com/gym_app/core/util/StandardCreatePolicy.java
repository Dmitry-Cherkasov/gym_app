package com.gym_app.core.util;

import com.gym_app.core.repo.TraineeRepository;
import com.gym_app.core.repo.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StandardCreatePolicy implements CreatePolicy{
    private final int PASSWORD_LENGTH = 10;
    private final int MIN_TRAINEE_AGE = 5;

    @Override
    public String getUserName(String firstName, String lastName) {
        return firstName+"."+lastName;
    }

    @Override
    public String getPassword(int length) {
        return PasswordCreator.createPassword(PASSWORD_LENGTH);
    }

}
