package com.gym_app.core.services;

import com.gym_app.core.dao.TrainerDao;
import com.gym_app.core.dto.Trainer;
import lombok.extern.slf4j.Slf4j; // Import for Lombok's logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j // Lombok annotation for logging
public class TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerService.class);
    private final TrainerDao trainerDao;

    @Autowired
    public TrainerService(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    // Create method with logging
    public void create(Trainer trainer) {
        log.info("Creating trainer: {}", trainer); // Log method entry
        if (trainer == null) {
            log.error("Failed to create trainer: Trainer argument is null");
            throw new IllegalArgumentException("Trainer argument cannot be null");
        }
        Trainer savedTrainer = trainerDao.save(trainer);
        log.info("Trainer created successfully with ID: {}", savedTrainer.getUserId()); // Log success
    }

    public void update(Trainer trainer, String[] updates) {
        log.info("Updating trainer: {}", trainer);
        if (updates == null || updates.length == 0) {
            log.error("Failed to update trainer: Update array is null or empty");
            throw new IllegalArgumentException("Update array cannot be null or empty");
        } else if (trainer == null) {
            log.error("Failed to update trainer: Trainer argument is null");
            throw new IllegalArgumentException("Trainer argument cannot be null");
        }
        trainerDao.update(trainer, updates);
        log.info("Trainer updated successfully: {}", trainer);
    }

    public void delete(long trainerId) {
        log.info("Deleting trainer with ID: {}", trainerId);
        Optional<Trainer> trainer = trainerDao.getById(trainerId);
        if (trainer.isPresent()) {
            trainerDao.delete(trainer.get());
            log.info("Trainer deleted successfully with ID: {}", trainerId);
        } else {
            log.error("Failed to delete trainer: Trainer with ID {} not found", trainerId);
            throw new NoSuchElementException("Id is not present in the persistence store");
        }
    }

    public Optional<Trainer> select(long id) {
        log.info("Selecting trainer with ID: {}", id);
        Optional<Trainer> trainer = trainerDao.getById(id);
        if (trainer.isEmpty()) {
            log.warn("Trainer with ID {} not found", id); // Use warn for not-found cases
            throw new NoSuchElementException("Id is not present in the persistence store");
        }
        log.info("Trainer selected: {}", trainer.get());
        return trainer;
    }
}
