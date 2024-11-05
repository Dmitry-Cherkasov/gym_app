package com.gym_app.core.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChangeLoginRequest {
    @NotNull(message = "User name cannot be null")
    private String userName;
    @NotNull(message = "Password cannot be null")
    private String oldPassword;
    @NotNull(message = "New password cannot be null")
    @Size (min = 10, max = 20, message = "Password must be between 10 and 20 characters")
    private String newPassword;
    @NotNull(message = "User type must be defined")
    private boolean isTrainee;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public boolean isTrainee() {
        return isTrainee;
    }

    public void setIsTrainee(boolean isTrainee) {
        this.isTrainee = isTrainee;
    }


}
