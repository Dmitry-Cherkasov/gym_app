package com.gym_app.core.configuration;

import com.gym_app.core.dao.TraineeJpaDaoImpl;
import com.gym_app.core.dao.TrainerJpaDaoImpl;
import com.gym_app.core.dto.Trainee;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.dto.User;
import com.gym_app.core.enums.TrainingType;
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


//@Configuration
public class Tester {
    @Autowired
    private TraineeJpaDaoImpl traineeJpaDao;
    @Autowired
    private TrainerJpaDaoImpl trainerJpaDao;
    @Autowired
    private TraineeFactory traineeFactory;
    @Autowired
    private TrainerFactory trainerFactory;
    @Autowired
    TrainingFactory trainingFactory;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    TrainingService trainingService;
    @Value("${storage.trainers.data.file.path}")
    private String trainerFilePath;
    @Value("${storage.trainees.data.file.path}")
    private String traineeFilePath;
    @Value("${storage.trainings.data.file.path}")
    private String trainingsFilePath;

    @PostConstruct
    public void config() {
        addTrainees();
        addTrainers();

    }

    private void addTrainees(){
        ArrayList<String> traineeData;
        try {
            traineeData = ResourceFileReader.readFile(traineeFilePath);
            for (String entry : traineeData) {
                String[] args = entry.split(",");
                Trainee trainee = traineeFactory.createUser(args[0], args[1], Boolean.parseBoolean(args[2]), LocalDate.parse(args[3]));
                trainee.setUserName(trainee.getFirstName() + "." + trainee.getLastName());
                trainee.setPassword(PasswordGenerator.createPassword(10));
                trainee.setAddress(args[4]);
                traineeJpaDao.save(trainee);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addTrainers(){
        ArrayList<String> trainerData;
        try {
            trainerData = ResourceFileReader.readFile(trainerFilePath);
            for (String entry : trainerData) {
                String[] args = entry.split(",");
                Trainer trainer = trainerFactory.createUser(args[0], args[1], Boolean.parseBoolean(args[2]), TrainingType.valueOf(args[3]));
                trainer.setUserName(trainer.getFirstName() + "." + trainer.getLastName());
                trainer.setPassword(PasswordGenerator.createPassword(10));
                trainerJpaDao.save(trainer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
