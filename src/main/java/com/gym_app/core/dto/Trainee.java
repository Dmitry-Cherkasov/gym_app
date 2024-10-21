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
    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Training> trainings = new ArrayList<>();

    public Trainee(){}

    public Trainee(String firstName, String lastName, String userName, String password, boolean isActive, LocalDate date, String address){
        super(firstName, lastName, userName, password, isActive);
        this.dateOfBirth = date;
        this.address = address;
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

    @Override
    public String toString() {
        return "Trainee{" + super.toString() +
                "dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                '}';
    }

}
