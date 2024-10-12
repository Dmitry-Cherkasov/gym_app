package com.gym_app.core.util;

import com.gym_app.core.dto.Trainee;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TraineeFactory implements UserFactory<Trainee> {


    @Override
    public Trainee createUser(String firstName, String lastName, boolean isActive, Object... extraArg) {
        LocalDate dateOfBirth = null;
        String address = null;
        if (extraArg.length > 0 && extraArg[0] instanceof LocalDate) {
            dateOfBirth = (LocalDate) extraArg[0];
        }
        if (extraArg.length > 1 && extraArg[1] instanceof String) {
            address = (String) extraArg[1];
        }
        return new Trainee(firstName, lastName, null, null, isActive, dateOfBirth, address);
    }
}
