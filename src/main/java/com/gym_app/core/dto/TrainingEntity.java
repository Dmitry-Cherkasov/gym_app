package com.gym_app.core.dto;

import jakarta.persistence.*;

import java.io.Serializable;

//@Entity
@Table(name = "TRAINING")
public class TrainingEntity extends Training implements Serializable {

    @ManyToOne
    @JoinColumn(name = "TRAINER_ID", nullable = false)
    private Trainer trainer;
    @ManyToOne
    @JoinColumn(name = "TRAINEE_ID", nullable = false)
    private Trainee trainee;

    public TrainingEntity() {
    }
}
