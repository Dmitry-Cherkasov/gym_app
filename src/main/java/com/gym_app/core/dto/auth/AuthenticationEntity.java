package com.gym_app.core.dto.auth;

public class AuthenticationEntity {
    private String userName;
    private String password;

    public AuthenticationEntity(){}


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
