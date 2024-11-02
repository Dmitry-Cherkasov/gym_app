package com.gym_app.core.dto.auth;

import java.time.LocalDate;

public class TraineeRegistrationRequest extends RegistrationRequest{
    private LocalDate birthDate;
    private String address;


    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
