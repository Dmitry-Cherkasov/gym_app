package com.gym_app.core.util;

import com.gym_app.core.dto.Training;
import com.gym_app.core.enums.TrainingType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TrainingFactory implements ServiceTypeFactory<Training> {

    @Override
    public Training createService(long consumerId, long supplierId, String serviceName, LocalDate serviceDate, int duration) {
        return new Training(consumerId, supplierId, serviceName, null, serviceDate, duration);
    }

    public Training createTraining(long consumerId, long supplierId, String serviceName, TrainingType trainingType, LocalDate serviceDate, int duration){
        return new Training(consumerId, supplierId, serviceName, trainingType, serviceDate, duration);
    }

}
