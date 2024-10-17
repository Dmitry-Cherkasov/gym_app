package com.gym_app.core.dto;

import com.gym_app.core.enums.TrainingType;
import jakarta.persistence.*;

//@Entity
@Table(name = "TRAINING_TYPE")
public class TrainingTypeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // or GenerationType.AUTO
    @Column(name = "ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRAINING_TYPE", nullable = false, unique = true)
    private TrainingType trainingType;

    public TrainingTypeEntity() {
    }

    public TrainingTypeEntity(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }


    @Override
    public String toString() {
        return "TrainingTypeEntity{" +
                "id=" + id +
                ", trainingType=" + trainingType +
                '}';
    }
}

