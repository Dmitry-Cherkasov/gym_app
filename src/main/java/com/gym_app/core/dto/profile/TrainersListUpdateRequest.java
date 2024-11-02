package com.gym_app.core.dto.profile;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TrainersListUpdateRequest {
    @NotNull(message = "User name cannot be null")
    private String traineeUsername;
    @NotNull(message = "Trainers list cannot be null")
    private List<String> trainersList = new ArrayList<>();

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    public List<String> getTrainersList() {
        return trainersList;
    }

    public void setTrainersList(List<String> trainersList) {
        this.trainersList = trainersList;
    }
}
