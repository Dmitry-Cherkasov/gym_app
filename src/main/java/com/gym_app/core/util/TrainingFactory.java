package com.gym_app.core.util;

import com.gym_app.core.dto.Training;
import com.gym_app.core.enums.TrainingType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TrainingFactory implements ServiceTypeFactory<Training, Long> {

    @Override
    public Training createService(Long consumerId, Long supplierId, String serviceName, LocalDate serviceDate, int duration, Object extraArg) {
        TrainingType trainingType;
        try{
            trainingType = TrainingType.valueOf(extraArg.toString().toUpperCase());
        }catch (IllegalArgumentException exception){
            throw new IllegalArgumentException("Illegal training type argument" + extraArg);
        }
        return new Training(consumerId, supplierId, serviceName, trainingType, serviceDate, duration);
    }

}
