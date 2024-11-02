package com.gym_app.core.util;

import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.enums.TrainingType;
import org.springframework.stereotype.Component;


@Component
public class TrainerFactory implements UserFactory<Trainer>{


    @Override
    public Trainer createUser(String firstName, String lastName, boolean isActive, Object... extraArg) {
        TrainingType trainingType = null;
        if (extraArg.length > 0) {
            if(extraArg[0] instanceof TrainingType){
                trainingType = (TrainingType) extraArg[0];
            }else{
            try {
                trainingType = TrainingType.valueOf(((String) extraArg[0]).toUpperCase());
            }catch (IllegalArgumentException exception){
                throw new IllegalArgumentException("Illegal training type argument: " + extraArg[0]);
            }
        }
        }
        return new Trainer(firstName, lastName, null, null, isActive, trainingType);
    }
}
