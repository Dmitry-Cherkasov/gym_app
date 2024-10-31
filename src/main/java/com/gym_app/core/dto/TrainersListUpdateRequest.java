package com.gym_app.core.dto;

import java.util.ArrayList;
import java.util.List;

public class TrainersListUpdateRequest {
        private String traineeUsername;
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
