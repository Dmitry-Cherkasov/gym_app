package com.gym_app.core.services;


import com.gym_app.core.dao.TraineeMapDaoImplementation;
import com.gym_app.core.dto.Trainee;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeService.class);
    private final TraineeMapDaoImplementation traineeDao;

    @Autowired
    public TraineeService(TraineeMapDaoImplementation traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainee create(Trainee trainee) {
        log.info("Creating trainee: {}", trainee);
        if (trainee == null) {
            log.error("Failed to create traineeId: Trainee argument is null");
            throw new IllegalArgumentException("Trainee argument cannot be null");
        }
            Trainee savedTrainee = traineeDao.save(trainee);
            log.info("Trainee created successfully with user name: {}", savedTrainee.getUserName());
            return savedTrainee;
    }

    public void update(Trainee trainee, String[] updates) {
        log.info("Updating trainee: {}", trainee);
        if (updates == null || updates.length == 0) {
            log.error("Failed to update trainee: Update array is null or empty");
            throw new IllegalArgumentException("Update array cannot be null or empty");
        } else if (trainee == null) {
            log.error("Failed to update trainee: Trainee argument is null");
            throw new IllegalArgumentException("Trainee argument cannot be null");
        }
        try {
            traineeDao.update(trainee, updates);
            log.info("Trainee updated successfully: {}", trainee);
        }catch (IllegalArgumentException exception){
            log.error("Failed to update trainee: {}", exception.getMessage());
            throw new IllegalArgumentException(exception.getMessage());
        }
    }

    public void delete(String userName) {
        log.info("Deleting trainee with UserName: {}", userName);
        Optional<Trainee> trainee = traineeDao.getById(userName);
        if (trainee.isPresent()) {
            traineeDao.delete(trainee.get());
            log.info("Trainee deleted successfully with UserName: {}", userName);
        } else {
            log.error("Failed to delete trainee: Trainee with UserName: {} not found", userName);
            throw new NoSuchElementException("Trainee is not present in the persistence store");
        }
    }

    public Optional<Trainee> select(String userName) {
        log.info("Selecting trainee with UserName: {}", userName);
        Optional<Trainee> trainee = traineeDao.getById(userName);
        if (trainee.isEmpty()) {
            log.warn("Trainee with UserName: {} not found", userName); // Use warn for not-found cases
            throw new NoSuchElementException("Trainee is not present in the persistence store");
        }
        log.info("Trainee selected: {}", trainee.get());
        return trainee;
    }
}