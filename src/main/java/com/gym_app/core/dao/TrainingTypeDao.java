package com.gym_app.core.dao;

import com.gym_app.core.enums.TrainingType;

import java.math.BigInteger;

public interface TrainingTypeDao extends Dao<TrainingType, Long>{
    String ALL = "SELECT * FROM TRAINING_TYPE";
    String BY_ID = "SELECT * FROM TRAINING_TYPE WHERE ID = ?";
    String BY_NAME = "SELECT * FROM TRAINING_TYPE WHERE TRAINING_TYPE_NAME = ?";
}
