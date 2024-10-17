package com.gym_app.core.dto;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table (name = "\"USER\"")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "USER_ID")
    private long id;
    @Column(name = "FIRST_NAME", length=50, nullable=false)
    private String firstName;
    @Column(name = "LAST_NAME", length=50, nullable=false)
    private String lastName;
    @Column(name = "USER_NAME", length=101, nullable=false, unique=true)
    private String userName;
    @Column(name = "PASSWORD", length=10, nullable=false)
    private String password;
    @Column(name = "IS_ACTIVE", nullable=false)
    private boolean isActive;

    public User(){}

    public User(String firstName, String lastName, String userName, String password, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "User{" +
                ", " + firstName + '\'' +
                ", " + lastName + '\'' +
                ", user= " + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getUserName(), user.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getUserName());
    }
}