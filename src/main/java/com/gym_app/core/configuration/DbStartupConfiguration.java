package com.gym_app.core.configuration;

import com.gym_app.core.dao.TrainingJpaDao;
import com.gym_app.core.dto.common.Trainee;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.services.TraineeDbService;
import com.gym_app.core.services.TrainerDBService;
import com.gym_app.core.services.TrainingService;
import com.gym_app.core.util.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;


@Configuration
@Profile("dev")
public class DbStartupConfiguration {
    @Autowired
    private TraineeFactory traineeFactory;
    @Autowired
    private TrainingJpaDao trainingJpaDao;
    @Autowired
    private TrainerFactory trainerFactory;
    @Autowired
    private TrainerDBService trainerService;
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
    @Autowired
    private ResourceFileReader reader;
    private ArrayList<Trainee> trainees;
    private ArrayList<Trainer> trainers;

    @PostConstruct
    public void config() {
        addTrainers();
        addTrainees();
        addTrainings();
    }


    private void addTrainees() {
        trainees = new ArrayList<>();
        ArrayList<String> traineeData;
        Random random = new Random();
        try {
            traineeData = reader.readFile(traineeFilePath);
            for (String entry : traineeData) {
                String[] args = entry.split(",");
                Trainee trainee = traineeFactory.createUser(args[0], args[1], Boolean.parseBoolean(args[2]), LocalDate.parse(args[3]));
                trainee.setUserName(trainee.getFirstName() + "." + trainee.getLastName());
                trainee.setPassword(PasswordGenerator.createPassword(10));
                trainee.setAddress(args[4]);
                trainee = traineeService.create(trainee);
                traineeService.addTrainerToList(trainee.getUserName(), trainee.getPassword(), trainers.get(random.nextInt(5)));
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
            trainerData = reader.readFile(trainerFilePath);
            for (String entry : trainerData) {
                String[] args = entry.split(",");
                Trainer trainer = trainerFactory.createUser(args[0], args[1], Boolean.parseBoolean(args[2]), TrainingType.valueOf(args[3]));
                trainer.setUserName(trainer.getFirstName() + "." + trainer.getLastName());
                trainer.setPassword(PasswordGenerator.createPassword(10));
                trainer = trainerService.create(trainer);
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
            try{
                traineeService.addTraining(trainee.getUserName(),trainee.getPassword(),
                        trainer, trainer.getSpecialization() + " traininig",
                        trainer.getSpecialization(), LocalDate.now().plusDays(2), 90);
            }catch (RuntimeException exception){
                throw new RuntimeException(exception.getMessage());
            }
        }
    }

}
