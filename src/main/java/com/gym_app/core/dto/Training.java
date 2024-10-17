package com.gym_app.core.dto;

import com.gym_app.core.enums.TrainingType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "TRAINING")
public class Training implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long trainingId;
    @Transient
    private long traineeId;
    @Transient
    private long trainerId;
    @Column(name = "TRAINING_NAME", nullable = false)
    String trainingName;
    @Column(name = "TRAINING_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    LocalDate trainingDate;
    @Column(name = "DURATION", nullable = false)
    int duration;
    @Column(name = "TRAINING_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainingType trainingType;
    @ManyToOne
    @JoinColumn(name = "TRAINER_ID", nullable = false)
    private Trainer trainer;
    @ManyToOne
    @JoinColumn(name = "TRAINEE_ID", nullable = false)
    private Trainee trainee;

    public Training() {
    }

    public Training(long trainingId, long traineeId, long trainerId, String trainingName, LocalDate trainingDate, int duration, TrainingType trainingType) {
        this.trainingId = trainingId;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.duration = duration;
        this.trainingType = trainingType;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(long trainingId) {
        this.trainingId = trainingId;
    }

    public long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(long traineeId) {
        this.traineeId = traineeId;
    }

    public long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(long trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Training(long traineeId, long trainerId, String trainingName, TrainingType trainingType, LocalDate trainingDate, int duration) {
        this.trainingType = trainingType;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }


}
