package com.gym_app.core.services;

import com.gym_app.core.dao.TrainingMapDaoImplementation;
import com.gym_app.core.dao.TrainingMapDaoImplementation;
import com.gym_app.core.dto.Training;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);
    private final TrainingMapDaoImplementation trainingDao;

    @Autowired
    public TrainingService(TrainingMapDaoImplementation trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Training create(Training training) {
        log.info("Creating training: {}", training);
        if (training == null) {
            log.error("Failed to create training: Training argument is null");
            throw new IllegalArgumentException("Training argument cannot be null");
        }
        Training savedTraining = trainingDao.save(training);
        log.info("Training created successfully with ID: {}", savedTraining.getServiceId());
        return savedTraining;
    }

    public void update(Training training, String[] updates) {
        log.info("Updating training: {}", training);
        if (updates == null || updates.length == 0) {
            log.error("Failed to update training: Update array is null or empty");
            throw new IllegalArgumentException("Update array cannot be null or empty");
        } else if (training == null) {
            log.error("Failed to update training: Training argument is null");
            throw new IllegalArgumentException("Training argument cannot be null");
        }
        trainingDao.update(training, updates);
        log.info("Training updated successfully: {}", training);
    }

    public void delete(long trainingId) {
        log.info("Deleting training with ID: {}", trainingId);
        Optional<Training> training = trainingDao.getById(trainingId);
        if (training.isPresent()) {
            trainingDao.delete(training.get());
            log.info("Training deleted successfully with ID: {}", trainingId);
        } else {
            log.error("Failed to delete training: Training with ID {} not found", trainingId);
            throw new NoSuchElementException("Id is not present in the persistence store");
        }
    }

    public Optional<Training> select(long id) {
        log.info("Selecting training with ID: {}", id);
        Optional<Training> training = trainingDao.getById(id);
        if (training.isEmpty()) {
            log.warn("Training with ID {} not found", id); // Use warn for not-found cases
            throw new NoSuchElementException("Id is not present in the persistence store");
        }
        log.info("Training selected: {}", training.get());
        return training;
    }
}
