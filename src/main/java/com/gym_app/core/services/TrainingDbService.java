package com.gym_app.core.services;

import com.gym_app.core.dao.Dao;
import com.gym_app.core.dao.TrainingJpaDao;
import com.gym_app.core.dto.Training;
import com.gym_app.core.util.TrainingUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TrainingDbService extends AbstractService<Training, Long> {
    private TrainingJpaDao trainingJpaDao;

    @Autowired
    public TrainingDbService(TrainingJpaDao trainingJpaDao) {
        this.trainingJpaDao = trainingJpaDao;
    }

    @Override
    protected Dao<Training, Long> getDao() {
        return trainingJpaDao;
    }

    public void setDao(TrainingJpaDao trainingJpaDao) {
        this.trainingJpaDao = trainingJpaDao;
    }

    public Training create(Training training) {
        if(!training.getTrainer().isActive()){
            throw new RuntimeException("Failed to create new training, trainer is inactive");
        }
        try {
            return trainingJpaDao.save(training);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to create new training: " + training);
        }
    }

    @Override
    public void update(Training oldTraining, String[] updates) {
        try {
            Training updatedTraining = TrainingUpdater.updateTraining(oldTraining, updates);
            trainingJpaDao.update(oldTraining,updatedTraining);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to update training: " + oldTraining, e);
        }
    }


}
