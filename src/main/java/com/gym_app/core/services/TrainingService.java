package com.gym_app.core.services;

import com.gym_app.core.dao.Dao;
import com.gym_app.core.dao.TrainingMapDaoImplementation;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.util.TrainingUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class TrainingService extends AbstractService<Training, Long>{
    private final TrainingMapDaoImplementation trainingDao;

    @Autowired
    public TrainingService(TrainingMapDaoImplementation trainingDao) {
        this.trainingDao = trainingDao;
    }


    @Override
    protected Dao<Training, Long> getDao() {
        return trainingDao;
    }

    @Override
    public void update(Training training, String[] updates) {
        try {
            Training updatedTraining = TrainingUpdater.updateTraining(training, updates);
            trainingDao.update(training,updatedTraining);
        }catch (RuntimeException e){
            throw new RuntimeException("Update failed for training:" + training + " with params: " + Arrays.toString(updates));
        }
    }
}
