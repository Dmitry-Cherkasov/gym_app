package com.gym_app.core.dto;

public class ChangeLoginRequest {
    private String userName;
    private String oldPassword;
    private String newPassword;
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

    public void setIsTrainee(boolean trainee) {
        isTrainee = trainee;
    }
}
