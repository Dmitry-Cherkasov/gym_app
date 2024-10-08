package com.gym_app.core.dto;

import java.time.LocalDate;
import java.util.Objects;

public class Trainee extends User{
    private long id;
    private LocalDate dateOfBirth;
    private String address;

    public Trainee(String firstName, String lastName, String userName, String password, boolean isActive, LocalDate date, String address){
        super(firstName, lastName, userName, password, isActive);
        this.dateOfBirth = date;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainee trainee = (Trainee) o;
        return getId() == trainee.getId() && Objects.equals(getDateOfBirth(), trainee.getDateOfBirth()) && Objects.equals(getAddress(), trainee.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateOfBirth(), getAddress());
    }
}
