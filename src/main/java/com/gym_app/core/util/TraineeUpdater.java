package com.gym_app.core.util;

import com.gym_app.core.dto.Trainee;

import java.time.LocalDate;

public class TraineeUpdater {


    public static Trainee updateTrainee (Trainee trainee, String[] params) {
        if (trainee == null) {
            throw new RuntimeException("Trainee object cannot be null.");
        }
        if (params.length < 7) {
            throw new RuntimeException("Invalid number of parameters. Expected 7 parameters.");
        }
        for (String element : params) {
            if (element == null) {
                throw new RuntimeException("Invalid parameter with Null value.");
            }
        }
        System.out.println("Trainee before: " + trainee);
        trainee.setFirstName(params[0]);
        trainee.setLastName(params[1]);
//        trainee.setUserName(params[2]);
//        trainee.setPassword(params[3]);
        trainee.setActive(Boolean.parseBoolean(params[4]));
        try {
            LocalDate dateOfBirth = LocalDate.parse(params[5]);
            trainee.setDateOfBirth(dateOfBirth);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format for dateOfBirth. Expected format: YYYY-MM-DD.", e);
        }
        trainee.setAddress(params[6]);
        System.out.println("Trainee after: " + trainee);
        return trainee;
    }
}
