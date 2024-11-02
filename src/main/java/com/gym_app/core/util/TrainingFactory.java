package com.gym_app.core.util;

import com.gym_app.core.dto.common.Trainee;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.enums.TrainingType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
public class TrainingFactory implements ServiceTypeFactory<Training, Long> {

    @Override
    public Training createService(Long consumerId, Long supplierId, String serviceName, LocalDate serviceDate, int duration, Object extraArg) {
        TrainingType trainingType;
        Training training = new Training();
        training.setTraineeId(consumerId);
        training.setTrainerId(supplierId);
        training.setTrainingDate(serviceDate);
        training.setTrainingName(serviceName);
        training.setDuration(duration);
        try{
            trainingType = TrainingType.valueOf(extraArg.toString().toUpperCase());
        }catch (IllegalArgumentException exception){
            throw new IllegalArgumentException("Illegal training type argument" + extraArg);
        }
        training.setTrainingType(trainingType);
        return training;
    }

    public Training createTraining(Trainee trainee, Trainer trainer){

        Random random = new Random();
        Training training = new Training();
                training.setTrainer(trainer);
                training.setTrainee(trainee);
                training.setTrainerId(trainer.getId());
                training.setTraineeId(trainee.getId());
                training.setTrainingName(trainer.getSpecialization().toString() + " training");
                training.setTrainingType(trainer.getSpecialization());
                training.setTrainingDate(LocalDate.now().plusDays(random.nextInt(7)));
                training.setDuration(90);

                return training;
    }

}
