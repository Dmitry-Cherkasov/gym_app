package com.gym_app.core.dto.common;

import jakarta.validation.constraints.NotNull;

public class ToggleActiveRequest {
    @NotNull(message = "User name cannot be null")
    private String username;
    @NotNull (message = "Active status cannot be null")
    private Boolean isActive;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
