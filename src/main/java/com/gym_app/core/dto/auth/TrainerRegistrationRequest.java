package com.gym_app.core.dto.auth;

import com.gym_app.core.enums.TrainingType;
import jakarta.validation.constraints.NotNull;

public class TrainerRegistrationRequest extends RegistrationRequest{
    @NotNull(message = "Specialization (Training Type) cannot be null")
    private TrainingType trainingType;

    public @NotNull(message = "Specialization (Training Type) cannot be null") TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(@NotNull(message = "Specialization (Training Type) cannot be null") TrainingType trainingType) {
        this.trainingType = trainingType;
    }
}
