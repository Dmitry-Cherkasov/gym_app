package com.gym_app.core.dao;

import com.gym_app.core.dto.User;

public interface UserDao extends Dao<User, Long> {
    String ALL = "SELECT * FROM USER";
    String BY_ID = "SELECT * FROM USER WHERE ID = ?";
    String BY_USERNAME = "SELECT * FROM USER WHERE USERNAME = ?";
    String INSERT = "INSERT INTO USER (FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, IS_ACTIVE) VALUES (?, ?, ?, ?, ?)";
    String DELETE = "DELETE FROM USER WHERE ID = ?";
    String UPDATE = "UPDATE USER SET FIRST_NAME = ?, LAST_NAME = ?, USERNAME = ?, PASSWORD = ?, IS_ACTIVE = ? WHERE ID = ?";
}
