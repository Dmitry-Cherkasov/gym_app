package com.gym_app.core.dto;

import com.gym_app.core.enums.TrainingType;

import java.util.Objects;

public class Trainer extends User{
    private TrainingType specialization;

    public Trainer(String firstName, String lastName, String userName, String password, boolean isActive, TrainingType specialization){
        super(firstName, lastName, userName, password, isActive);
        this.specialization = specialization;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }


    @Override
    public String toString() {
        return "Trainer{" + super.toString() +
                " " + specialization + '\'' +
                '}';
    }


}
