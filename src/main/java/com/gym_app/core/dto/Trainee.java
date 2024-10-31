package com.gym_app.core.dto;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TRAINEE")
public class Trainee extends User {
    @PrimaryKeyJoinColumn(name = "TRAINEE_ID")
    @OneToOne
    private User user;
    @Column (name = "DATE_OF_BIRTH")
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;
    @Column(name = "ADDRESS", length = 100)
    private String address;
    @OneToMany(mappedBy = "trainee", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<Training> trainings = new ArrayList<>();
    @ManyToMany
    @JoinTable(name="TRAINEE_TRAINER",
            joinColumns=@JoinColumn(name="TRAINEE_ID"),
            inverseJoinColumns=@JoinColumn(name="TRAINER_ID"))
    private List<Trainer> trainers;

    public Trainee(){
        trainers = new ArrayList<>();
        trainings = new ArrayList<>();
    }

    public Trainee(String firstName, String lastName, String userName, String password, boolean isActive, LocalDate date, String address){
        super(firstName, lastName, userName, password, isActive);
        this.dateOfBirth = date;
        this.address = address;
        trainers = new ArrayList<>();
        trainings = new ArrayList<>();
    }


    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addTraining(Training training){
        if(!trainings.contains(training)){
            trainings.add(training);
            training.setTrainee(this);
        }
    }

    public void addTrainer(Trainer trainer) {
        if (!trainers.contains(trainer)) {
            trainers.add(trainer);
            trainer.getTrainees().add(this);
        }
    }

    public void removeTrainer(Trainer trainer) {
        trainers.remove(trainer);
        trainer.getTrainees().remove(this);
    }

    @Override
    public String toString() {
        return "Trainee{" + super.toString() +
                "dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                '}';
    }

}
