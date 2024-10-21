package com.gym_app.core.services;

import com.gym_app.core.dao.JpaDao;
import com.gym_app.core.dao.TraineeJpaDaoImpl;
import com.gym_app.core.dao.TrainingJpaDao;
import com.gym_app.core.dto.Trainee;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.dto.Training;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.util.TraineeUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class TraineeDbService extends AbstractDbService<Trainee>{

    private final TraineeJpaDaoImpl traineeJpaDao;
    private TrainingJpaDao trainingJpaDao;

    @Autowired
    public TraineeDbService(TraineeJpaDaoImpl traineeJpaDao, TrainingJpaDao trainingJpaDao) {
        this.traineeJpaDao = traineeJpaDao;
        this.trainingJpaDao = trainingJpaDao;
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

    public List<Training> getTraineeTrainings(String username, String password, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for trainee with username: " + username);
        }
        try{
            return traineeJpaDao.getTrainingsByCriteria(username,fromDate,toDate,trainerName,trainingType);
        }catch (RuntimeException e){
            throw new RuntimeException("Error calling trainings list", e);
        }
    }

    public List<Trainer> getAvailableTrainers(String username, String password) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for trainee with username: " + username);
        }
        try{
            return traineeJpaDao.getAvailableTrainers(username);
        }catch (RuntimeException e){
            throw new RuntimeException("Error calling trainings list", e);
        }
    }

    public Training addTraining(String username, String password, String trainingName, TrainingType trainingType, LocalDate date, int duration){
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for trainee with username: " + username);
        }
        Training training = new Training();
        try{
            Trainee trainee = traineeJpaDao.getByUserName(username).get();
            training.setTraineeId(trainee.getId());
            training.setTrainingName(trainingName);
            training.setTrainingType(trainingType);
            training.setTrainingDate(date);
            training.setDuration(duration);

            return trainingJpaDao.save(training);
        }catch (RuntimeException e){
            throw new RuntimeException("Error with adding new training: " + training, e);
        }

    }

}
