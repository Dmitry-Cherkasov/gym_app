package com.gym_app.core.dto.traininig;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TrainingCreateRequest {
    @NotNull(message = "Trainee user name cannot be null")
    private String traineeUsername;
    @NotNull(message = "Trainer user name cannot be null")
    private String trainerUsername;
    @NotNull(message = "Training name cannot be null")
    private String trainingName;
    @NotNull(message = "Training date cannot be null")
    private LocalDate trainingDate;
    @NotNull(message = "Training duration cannot be null")
    private int trainingDuration;

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public int getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(int trainingDuration) {
        this.trainingDuration = trainingDuration;
    }
}
