package com.gym_app.core.services;

import com.gym_app.core.dao.JpaDao;
import com.gym_app.core.dao.TrainerJpaDaoImpl;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.util.TrainerUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class TrainerDBService extends AbstractDbService<Trainer>{

    private final TrainerJpaDaoImpl trainerJpaDao;

    @Autowired
    public TrainerDBService(TrainerJpaDaoImpl trainerJpaDaoDao) {
        this.trainerJpaDao = trainerJpaDaoDao;
    }


    @Override
    protected JpaDao<Trainer, Long> getDao() {
        return trainerJpaDao;
    }

    @Override
    protected String getTypeName() {
        return "trainer";
    }

    @Override
    protected Trainer updateUser(Trainer trainer, String[] updates) {
        return TrainerUpdater.updateTrainer(trainer, updates);
    }
}
