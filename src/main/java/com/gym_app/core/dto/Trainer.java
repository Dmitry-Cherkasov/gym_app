package com.gym_app.core.dto;

import com.gym_app.core.enums.TrainingType;
import jakarta.persistence.*;


@Entity
@Table(name = "TRAINER")
public class Trainer extends User{
    @Column(name = "SPECIALIZATION", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainingType specialization;

    public Trainer(){}

    public Trainer(String firstName, String lastName, String userName, String password, boolean isActive, TrainingType specialization) {
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
