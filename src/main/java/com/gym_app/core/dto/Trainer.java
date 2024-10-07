package com.gym_app.core.dto;

import com.gym_app.core.enums.TrainingType;

public class Trainer extends User{
    private TrainingType specialization;

    public Trainer(UserData userData, TrainingType specialization){
        super(userData);
        this.specialization = specialization;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

}
