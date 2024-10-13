package com.gym_app.core.services;

import com.gym_app.core.dao.Dao;
import com.gym_app.core.dao.TrainingMapDaoImplementation;
import com.gym_app.core.dto.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
