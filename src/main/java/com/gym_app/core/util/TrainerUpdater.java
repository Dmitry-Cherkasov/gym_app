package com.gym_app.core.util;

import com.gym_app.core.dto.Trainer;
import com.gym_app.core.enums.TrainingType;

public class TrainerUpdater {
    
    public static Trainer updateTrainer(Trainer trainer, String[] params){
        if (trainer == null) {
            throw new RuntimeException("trainer object cannot be null.");
        }
        if (params.length < 6) {
            throw new RuntimeException("Invalid number of parameters. Expected 6 parameters.");
        }
        for (String element : params) {
            if (element == null) {
                throw new RuntimeException("Invalid parameter with Null value.");
            }
        }
        trainer.setFirstName(params[0]);
        trainer.setLastName(params[1]);
//        trainer.setUserName(params[2]);
//        trainer.setPassword(params[3]);
        trainer.setActive(Boolean.parseBoolean(params[4]));
        try {
            TrainingType trainingType = TrainingType.valueOf(params[5]);
            trainer.setSpecialization(trainingType);
        } catch (Exception e) {
            throw new RuntimeException("Invalid training type value: " +params[5]);
        }

        return trainer;
    }
}

