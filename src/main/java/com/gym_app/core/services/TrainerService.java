package com.gym_app.core.services;

import com.gym_app.core.dao.Dao;
import com.gym_app.core.dao.TrainerMapDaoImplementation;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.util.TrainerUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TrainerService extends AbstractService<Trainer, String>{
    private final TrainerMapDaoImplementation trainerDao;

    @Autowired
    public TrainerService(TrainerMapDaoImplementation trainerDao) {
        this.trainerDao = trainerDao;
    }


    @Override
    protected Dao<Trainer, String> getDao() {
        return trainerDao;
    }

    @Override
    public void update(Trainer entity, String[] updates) {
        Trainer updatedTrainer = TrainerUpdater.updateTrainer(entity, updates);
        trainerDao.update(entity, updatedTrainer);
    }
}
