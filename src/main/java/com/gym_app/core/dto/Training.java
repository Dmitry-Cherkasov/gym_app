package com.gym_app.core.dto;

import com.gym_app.core.enums.TrainingType;

import java.time.LocalDate;

public class Training extends ServiceType {
    private TrainingType trainingType;

    public Training(User trainee , User trainer, String trainingName, TrainingType trainingType, LocalDate trainingDate, int duration) {
        super(trainee, trainer, trainingName, trainingDate, duration);
        this.trainingType = trainingType;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    @Override
    public String toString() {
        return "Training{" + super.toString() +
                "trainingType=" + trainingType +
                ", trainingName='" + serviceName + '\'' +
                ", trainingDate=" + serviceDate +
                ", duration=" + duration +
                '}';
    }
}
