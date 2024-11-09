package com.gym_app.core.services;

import com.gym_app.core.dao.JpaDao;
import com.gym_app.core.dao.TrainerJpaDaoImpl;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.util.TrainerUpdater;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@Transactional
public class TrainerDBService extends AbstractDbService<Trainer> {

    private final TrainerJpaDaoImpl trainerJpaDao;


    @Autowired
    public TrainerDBService(TrainerJpaDaoImpl trainerJpaDao) {
        this.trainerJpaDao = trainerJpaDao;
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
    public Trainer updateUser(Trainer trainer, String[] updates) {
        return TrainerUpdater.updateTrainer(trainer, updates);
    }

    public List<Training> getTrainerTrainings(String username, String password, LocalDate fromDate, LocalDate toDate, String traineeName, TrainingType trainingType) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for trainee with username: " + username);
        }
        try {
            return trainerJpaDao.getTrainingsByCriteria(username, fromDate, toDate, traineeName, trainingType);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error calling trainings list", e);
        }
    }
}
