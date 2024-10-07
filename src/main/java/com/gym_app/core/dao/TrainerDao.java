package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainer;

public interface TrainerDao extends Dao<Trainer, Long> {
    String ALL = "SELECT * FROM TRAINER";
    String BY_ID = "SELECT * FROM TRAINER WHERE ID = ?";
    String INSERT = "INSERT INTO TRAINER (USER_ID, SPECIALIZATION_ID) VALUES (?, ?)";
    String DELETE = "DELETE FROM TRAINER WHERE ID = ?";
    String UPDATE = "UPDATE TRAINER SET SPECIALIZATION_ID WHERE ID = ?";

}
