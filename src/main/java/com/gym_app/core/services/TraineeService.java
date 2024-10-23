package com.gym_app.core.services;


import com.gym_app.core.dao.Dao;
import com.gym_app.core.dao.TraineeMapDaoImplementation;
import com.gym_app.core.dto.Trainee;
import com.gym_app.core.util.TraineeUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class TraineeService extends AbstractService<Trainee, String> {
    private final TraineeMapDaoImplementation traineeDao;

    @Autowired
    public TraineeService(TraineeMapDaoImplementation traineeDao) {
        this.traineeDao = traineeDao;
    }


    @Override
    protected Dao<Trainee, String> getDao() {
        return traineeDao;
    }

    @Override
    public void update(Trainee entity, String[] updates) {
        try{
        Trainee updatedTrainee = TraineeUpdater.updateTrainee(entity, updates);
        traineeDao.update(entity, updatedTrainee);
        }catch (Exception exception){
            throw new RuntimeException("Update failed for trainee:" + entity + " with params: " + Arrays.toString(updates));
        }

    }
}
