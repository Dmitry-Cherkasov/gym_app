package com.gym_app.core.services;


import com.gym_app.core.dao.Dao;
import com.gym_app.core.dao.TraineeMapDaoImplementation;
import com.gym_app.core.dto.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
