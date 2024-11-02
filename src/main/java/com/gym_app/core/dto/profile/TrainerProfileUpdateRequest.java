package com.gym_app.core.dto.profile;

import com.gym_app.core.enums.TrainingType;
import jakarta.validation.constraints.NotNull;


public class TrainerProfileUpdateRequest {
    @NotNull(message = "User name cannot be null")
    private String userName;
    @NotNull (message = "First name cannot be null")
    private String firstName;
    @NotNull (message = "Last name cannot be null")
    private String lastName;
    private TrainingType specialization;
    @NotNull (message = "Active status cannot be null")
    private boolean isActive;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }
}
