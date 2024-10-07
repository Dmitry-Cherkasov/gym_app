package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainee;

public interface TraineeDao extends Dao<Trainee, Long> {
    String ALL = "SELECT * FROM TRAINEE";
    String BY_ID = "SELECT * FROM TRAINEE WHERE ID = ?";
    String INSERT = "INSERT INTO TRAINEE (USER_ID, DATE_OF_BIRTH, ADDRESS) VALUES (?, ?, ?)";
    String DELETE = "DELETE FROM TRAINEE WHERE ID = ?";
    String UPDATE = "UPDATE TRAINEE SET USER_ID = ?, DATE_OF_BIRTH = ?, ADDRESS = ? WHERE ID = ?";
}
