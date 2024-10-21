package com.gym_app.core.configuration;

import com.gym_app.core.dao.TraineeJpaDaoImpl;
import com.gym_app.core.dao.TrainerJpaDaoImpl;
import com.gym_app.core.dao.TrainingJpaDao;
import com.gym_app.core.dto.Trainee;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.dto.Training;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.services.TraineeDbService;
import com.gym_app.core.services.TraineeService;
import com.gym_app.core.services.TrainerService;
import com.gym_app.core.services.TrainingService;
import com.gym_app.core.util.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;


@Configuration
public class DbStartupConfiguration {
    @Autowired
    private TraineeJpaDaoImpl traineeJpaDao;
    @Autowired
    private TrainerJpaDaoImpl trainerJpaDao;
    @Autowired
    private TraineeFactory traineeFactory;
    @Autowired
    private TrainingJpaDao trainingJpaDao;
    @Autowired
    private TrainerFactory trainerFactory;
    @Autowired
    private TrainingFactory trainingFactory;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TraineeDbService traineeService;
    @Autowired
    private TrainingService trainingService;
    @Value("${storage.trainers.data.file.path}")
    private String trainerFilePath;
    @Value("${storage.trainees.data.file.path}")
    private String traineeFilePath;
    @Value("${storage.trainings.data.file.path}")
    private String trainingsFilePath;

    private ArrayList<Trainee> trainees;
    private ArrayList<Trainer> trainers;

    @PostConstruct
    public void config() {
//        addTrainees();
//        addTrainers();
//        addTrainings();
    }

    private void addTrainees() {
        trainees = new ArrayList<>();
        ArrayList<String> traineeData;
        try {
            traineeData = ResourceFileReader.readFile(traineeFilePath);
            for (String entry : traineeData) {
                String[] args = entry.split(",");
                Trainee trainee = traineeFactory.createUser(args[0], args[1], Boolean.parseBoolean(args[2]), LocalDate.parse(args[3]));
                trainee.setUserName(trainee.getFirstName() + "." + trainee.getLastName());
                trainee.setPassword(PasswordGenerator.createPassword(10));
                trainee.setAddress(args[4]);
                trainee = traineeJpaDao.save(trainee);
                trainees.add(trainee);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addTrainers() {
        trainers = new ArrayList<>();
        ArrayList<String> trainerData;
        try {
            trainerData = ResourceFileReader.readFile(trainerFilePath);
            for (String entry : trainerData) {
                String[] args = entry.split(",");
                Trainer trainer = trainerFactory.createUser(args[0], args[1], Boolean.parseBoolean(args[2]), TrainingType.valueOf(args[3]));
                trainer.setUserName(trainer.getFirstName() + "." + trainer.getLastName());
                trainer.setPassword(PasswordGenerator.createPassword(10));
                trainer = trainerJpaDao.save(trainer);
                trainers.add(trainer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addTrainings() {
        Random random = new Random();
        for(int i=0; i < 10; i++){
            Trainee trainee = trainees.get(random.nextInt(trainees.size()));
            Trainer trainer = trainers.get(random.nextInt(trainers.size()));
            Training training = trainingFactory.createService(
                    trainee.getId(),
                    trainer.getId(),
                    trainer.getSpecialization().name() + " training",
                    LocalDate.now().plusDays(i),
                    90,
                    trainer.getSpecialization());
            training.setTrainer(trainer);
            training.setTrainee(trainee);
            try{
                trainingJpaDao.save(training);
            }catch (RuntimeException exception){
                throw new RuntimeException(exception.getMessage());
            }
        }
    }

}
