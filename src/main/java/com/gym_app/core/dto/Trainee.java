package com.gym_app.core.dto;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "TRAINEE")
@PrimaryKeyJoinColumn(name = "TRAINEE_ID")
public class Trainee extends User {
    @Column (name = "DATE_OF_BIRTH")
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;
    @Column(name = "ADDRESS", length = 100)
    private String address;

    public Trainee(){}

    public Trainee(String firstName, String lastName, String userName, String password, boolean isActive, LocalDate date, String address){
        super(firstName, lastName, userName, password, isActive);
        this.dateOfBirth = date;
        this.address = address;
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
