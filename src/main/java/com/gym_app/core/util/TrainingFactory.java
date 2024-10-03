package com.gym_app.core.util;

import com.gym_app.core.dto.ServiceType;
import com.gym_app.core.dto.Training;
import com.gym_app.core.dto.User;
import com.gym_app.core.enums.TrainingType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TrainingFactory implements ServiceTypeFactory {

    @Override
    public ServiceType createService(User consumer, User supplier, String serviceName, LocalDate serviceDate, int duration) {
        return new Training(consumer, supplier, serviceName, null, serviceDate, duration);
    }

    public Training createTraining(User trainee, User trainer, String trainingName, TrainingType trainingType, LocalDate trainingDate, int duration) {
        return new Training(trainee, trainer, trainingName, trainingType, trainingDate, duration);
    }
}
