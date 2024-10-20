package com.gym_app.core.util;

import com.gym_app.core.dto.Training;
import com.gym_app.core.enums.TrainingType;

import java.time.LocalDate;

public class TrainingUpdater {
    
    public static Training updateTraining(Training training, String[] params){
        if (training == null) {
            throw new RuntimeException("Training object cannot be null.");
        }
        if (params.length < 6) {
            throw new RuntimeException("Invalid number of parameters. Expected 6 parameters.");
        }
        for (String element : params) {
            if (element == null) {
                throw new RuntimeException("Invalid parameter with Null value.");
            }
        }
        training.setTraineeId(Long.parseLong(params[0]));
        training.setTrainerId(Long.parseLong(params[1]));
        training.setTrainingName(params[2]);

        training.setDuration(Integer.parseInt(params[5]));
        try {
            TrainingType trainingType = TrainingType.valueOf(params[3]);
            training.setTrainingType(trainingType);
        } catch (Exception e) {
            throw new RuntimeException("Invalid training type value: " +params[3]);
        }
        try {
            training.setTrainingDate(LocalDate.parse(params[4]));
        } catch (Exception e) {
            throw new RuntimeException("Invalid date value: " +params[4]);
        }

        return training;
    }
}
