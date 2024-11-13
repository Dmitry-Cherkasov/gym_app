package com.gym_app.core.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



public class RegistrationRequest {
    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "Fist name must be between 2 and 50 characters")
    private String firstName;
    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;


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

}
