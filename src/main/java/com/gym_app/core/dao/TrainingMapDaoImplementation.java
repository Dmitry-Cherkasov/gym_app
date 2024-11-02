package com.gym_app.core.dao;


import com.gym_app.core.dto.common.Training;

import com.gym_app.core.repo.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class TrainingMapDaoImplementation implements Dao<Training, Long>{
    private final TrainingRepository trainingRepository;

    @Autowired
    public TrainingMapDaoImplementation(TrainingRepository trainingRepository){
        this.trainingRepository = trainingRepository;
    }
    

    @Override
    public Optional<Training> getById(Long id) {
        return Optional.ofNullable(trainingRepository .get(id));
    }

    @Override
    public List<Training> getAll() {
        return new ArrayList<>(trainingRepository .values());
    }

    @Override
    public Training save(Training training) {
        Random random = new Random();
        long id = random.nextLong(1000);
        while (trainingRepository .containsKey(id)) {
            id = random.nextLong(1000);
        }
        training.setTrainingId(id);
        trainingRepository.put(id, training);
        return training;
    }

    @Override
    public void delete(Training training) {
        trainingRepository.values().removeIf(existingTraining -> existingTraining.getTrainingId() == (training.getTrainingId()));
    }


    @Override
    public void update(Training training, Training updatedtraining) {
        Training existingTraining = trainingRepository .get(training.getTrainingId());
        if (updatedtraining == null) {
            throw new IllegalArgumentException("Invalid parameter with Null value.");
        }
        if (existingTraining != null) {
            trainingRepository.put(training.getTrainingId(), updatedtraining);
        } else {
            throw new IllegalArgumentException("Trainer not found with user name: " + training.getTrainingId());
        }
    }
}
