package com.gym_app.core.services;

import com.gym_app.core.dao.JpaDao;
import com.gym_app.core.dao.TraineeJpaDaoImpl;
import com.gym_app.core.dao.TrainerJpaDaoImpl;
import com.gym_app.core.dao.TrainingJpaDao;
import com.gym_app.core.dto.common.Trainee;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.util.TraineeUpdater;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
public class TraineeDbService extends AbstractDbService<Trainee>{

    private final TraineeJpaDaoImpl traineeJpaDao;
    private TrainingJpaDao trainingJpaDao;
    private TrainerJpaDaoImpl trainerJpaDao;

    @Autowired
    public TraineeDbService(TraineeJpaDaoImpl traineeJpaDao, TrainingJpaDao trainingJpaDao, TrainerJpaDaoImpl trainerJpaDao) {
        this.traineeJpaDao = traineeJpaDao;
        this.trainingJpaDao = trainingJpaDao;
        this.trainerJpaDao = trainerJpaDao;
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
    public boolean delete(String username, String password) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for trainee with username: " + username);
        }
        try {
            Trainee trainee = traineeJpaDao.getByUserName(username)
                    .orElseThrow(() -> new RuntimeException("Failed to find trainee with username: " + username));

            List<Training> trainings = trainee.getTrainings();
            if (trainings != null && !trainings.isEmpty()) {
                trainings.forEach(training -> trainingJpaDao.delete(training));
            }

            List<Trainer> trainers = trainee.getTrainers();
            if(trainers != null && !trainers.isEmpty()){
                trainers.forEach(trainer -> {
                    trainee.removeTrainer(trainer);
                    trainerJpaDao.save(trainer);
                });
            }
            trainee.getTrainings().clear();

            traineeJpaDao.save(trainee);

            traineeJpaDao.delete(trainee);
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error occurred while deleting trainee with username: " + username, e);
        }
    }

    @Override
    public Trainee updateUser(Trainee trainee, String[] updates) {
        Trainee updated = TraineeUpdater.updateTrainee(trainee, updates);
        try {
            traineeJpaDao.update(trainee, updated);
        }catch (RuntimeException e){
            throw new RuntimeException("Failed to update trainee " + trainee.getUserName(), e);
        }
        return traineeJpaDao.getByUserName(trainee.getUserName()).
                orElseThrow(()-> new RuntimeException("Failed to update trainee " + trainee.getUserName()));
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

    public Training addTraining(String username, String password, Trainer trainer, String trainingName, TrainingType trainingType, LocalDate date, int duration){
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for trainee with username: " + username);
        }
        try{
            Training training = new Training();
            Trainee trainee = traineeJpaDao.getByUserName(username).orElseThrow(
                    ()->new RuntimeException("Not found trainee with username: " + username)
            );
            training.setTrainer(trainer);
            training.setTrainee(trainee);
            training.setTrainingName(trainingName);
            training.setTrainingType(trainingType);
            training.setTrainingDate(date);
            training.setDuration(duration);
            training = trainingJpaDao.save(training);

            trainee.addTraining(training);
            traineeJpaDao.save(trainee);

            return training;
        }catch (RuntimeException e){
            throw new RuntimeException("Error with adding new training", e);
        }

    }

    public void addTrainerToList(String username, String password, Trainer trainer) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for trainee with username: " + username);
        }
        try{
            Trainee trainee = traineeJpaDao.getByUserName(username)
                    .orElseThrow(() -> new RuntimeException ("Trainee not found"));

            trainee.addTrainer(trainer);
            traineeJpaDao.save(trainee);
        }catch (RuntimeException e){
            throw new RuntimeException("Error with adding new trainer: " + trainer, e);
        }
}

    public void removeTrainerFromList(String username, String password, Trainer trainer) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for trainee with username: " + username);
        }
        try{
            Trainee trainee = traineeJpaDao.getByUserName(username)
                    .orElseThrow(() -> new RuntimeException ("Trainee not found"));
            trainee.removeTrainer(trainer);
            traineeJpaDao.save(trainee);
        }catch (RuntimeException e){
            throw new RuntimeException("Error with removing trainer from trainee's list: " + trainer, e);
        }
    }

    public List<Trainer> getTraineesTrainers(String username, String password) {
        if (!authenticate(username, password)) {
            throw new SecurityException("Authentication failed for trainee with username: " + username);
        }
        Trainee trainee = traineeJpaDao.getByUserName(username)
                .orElseThrow(() -> new RuntimeException("Trainee not found"));
        return trainee.getTrainers();
    }

}
