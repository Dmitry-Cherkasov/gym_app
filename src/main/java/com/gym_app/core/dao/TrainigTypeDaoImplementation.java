package com.gym_app.core.dao;

import com.gym_app.core.enums.TrainingType;

import java.util.List;
import java.util.Optional;

public class TrainigTypeDaoImplementation implements TrainingTypeDao {
    @Override
    public Optional<TrainingType> getById(Long aLong) {

        return Optional.empty();
    }

    @Override
    public List<TrainingType> getAll() {
        return List.of();
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        return null;
    }

    @Override
    public void delete(TrainingType trainingType) {

    }
}

