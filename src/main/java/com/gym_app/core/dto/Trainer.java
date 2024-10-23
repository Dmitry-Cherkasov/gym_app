package com.gym_app.core.dto;

import com.gym_app.core.enums.TrainingType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "TRAINER")
public class Trainer extends User{
    @PrimaryKeyJoinColumn(name = "TRAINER_ID")
    @OneToOne
    private User user;
    @Column(name = "SPECIALIZATION", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainingType specialization;
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Training> trainings = new ArrayList<>();
    @ManyToMany(mappedBy = "trainers")
    private List<Trainee> trainees;


    public Trainer(){
        trainees = new ArrayList<>();
        trainings = new ArrayList<>();
    }

    public Trainer(String firstName, String lastName, String userName, String password, boolean isActive, TrainingType specialization) {
        super(firstName, lastName, userName, password, isActive);
        this.specialization = specialization;
        trainees = new ArrayList<>();
        trainings = new ArrayList<>();
    }


    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Trainee> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<Trainee> trainees) {
        this.trainees = trainees;
    }

    @Override
    public String toString() {
        return "Trainer{" + super.toString() +
                " " + specialization + '\'' +
                '}';
    }


}
