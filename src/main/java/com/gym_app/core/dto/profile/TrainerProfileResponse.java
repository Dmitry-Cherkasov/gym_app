package com.gym_app.core.dto.profile;

import com.gym_app.core.enums.TrainingType;

import java.util.List;

public class TrainerProfileResponse {
    private String userName;
    private String firstName;
    private String lastName;
    private TrainingType specialization;
    private boolean isActive;
    private List<TraineeSummary> trainees;



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

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public List<TraineeSummary> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<TraineeSummary> trainees) {
        this.trainees = trainees;
    }
}