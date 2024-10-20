package com.gym_app.core.dao;

import com.gym_app.core.dto.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TrainingJpaDao implements Dao<Training, Long> {
    @PersistenceContext
    private EntityManager entityManager;

    public TrainingJpaDao() {
    }

    @Override
    public Optional<Training> getById(Long id) {
        Training training = entityManager.find(Training.class, id);
        return Optional.ofNullable(training);
    }


    @Override
    public List<Training> getAll() {
        try {
            String query = "SELECT t FROM Training t";
            return entityManager.createQuery(query, Training.class).getResultList();
        } catch (Exception exception) {
            throw new RuntimeException("Error quering trainings list: " + exception.getMessage(), exception);
        }
    }

    @Override
    public Training save(Training training) {
        try {
            if (training.getTrainingId() != null) {
                return entityManager.merge(training);
            } else {
                entityManager.persist(training);
                return training;
            }
        } catch (Exception e) {

            throw new RuntimeException("Error saving training: " + training);
        }
    }

    @Override
    public void update(Training oldEntity, Training updatedEntity) {
        if (oldEntity == null || updatedEntity == null) {
            throw new RuntimeException("Error updating training with null entity");
        }

        Training managedOldEntity = entityManager.find(Training.class, oldEntity.getTrainingId());

        if (managedOldEntity != null) {
            updatedEntity.setTrainingId(managedOldEntity.getTrainingId());
            entityManager.merge(updatedEntity);
        } else {
            throw new RuntimeException("No such entity in persistence with ID: " + oldEntity.getTrainingId());
        }
    }

    @Override
    public void delete(Training training) {
        removeTraining(training);
    }

    private void removeTraining(Training training) {
        try {
            if (entityManager.contains(training)) {
                entityManager.remove(training);
            } else {
                entityManager.remove(entityManager.merge(training)); // Remove if detached
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting training: " + training);
        }
    }
}
