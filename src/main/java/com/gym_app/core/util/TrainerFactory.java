package com.gym_app.core.util;

import com.gym_app.core.dto.Trainer;
import com.gym_app.core.enums.TrainingType;
import org.springframework.stereotype.Component;


@Component
public class TrainerFactory implements UserFactory<Trainer>{


    @Override
    public Trainer createUser(String firstName, String lastName, boolean isActive, Object... extraArg) {
        TrainingType trainingType = null;
        if (extraArg.length > 0 && extraArg[0] instanceof String) {
            try {
                trainingType = TrainingType.valueOf((String) extraArg[0]);
            }catch (IllegalArgumentException exception){
                throw new IllegalArgumentException("Illegal training type argument: " + extraArg[0]);
            }
        }
        return new Trainer(firstName, lastName, null, null, isActive, trainingType);
    }
}
