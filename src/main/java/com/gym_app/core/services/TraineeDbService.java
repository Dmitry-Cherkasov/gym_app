package com.gym_app.core.services;

import com.gym_app.core.dao.JpaDao;
import com.gym_app.core.dao.TraineeJpaDaoImpl;
import com.gym_app.core.dto.Trainee;
import com.gym_app.core.util.TraineeUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TraineeDbService extends AbstractDbService<Trainee>{

    private final TraineeJpaDaoImpl traineeJpaDao;

    @Autowired
    public TraineeDbService(TraineeJpaDaoImpl traineeJpaDao) {
        this.traineeJpaDao = traineeJpaDao;
    }


    @Override
    protected JpaDao<Trainee, Long> getDao() {
        return traineeJpaDao;
    }

    @Override
    protected String getTypeName() {
        return "trainee";
    }

    @Override
    protected Trainee updateUser(Trainee trainee, String[] updates) {
        return TraineeUpdater.updateTrainee(trainee, updates);
    }

}
