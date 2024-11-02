package com.gym_app.core.dto.profile;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TraineeProfileUpdateRequest {
    @NotNull (message = "User name cannot be null")
    private String userName;
    @NotNull (message = "First name cannot be null")
    private String firstName;
    @NotNull (message = "Last name cannot be null")
    private String lastName;
    private LocalDate dateOfBirth;
    @NotNull (message = "Active status cannot be null")
    private boolean isActive;
    private String address;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate birthDate) {
        this.dateOfBirth = birthDate;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
