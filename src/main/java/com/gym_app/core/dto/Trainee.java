package com.gym_app.core.dto;

import java.time.LocalDate;

public class Trainee extends User{
    private LocalDate dateOfBirth;
    private String address;

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
                ", address='" + address + '\'' +
                '}';
    }
}
