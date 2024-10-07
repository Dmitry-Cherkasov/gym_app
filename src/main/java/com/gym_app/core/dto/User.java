package com.gym_app.core.dto;


public abstract class User {
    private UserData userData;

    public User(UserData userData){
        this.userData = userData;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}