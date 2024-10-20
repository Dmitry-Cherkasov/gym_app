package com.gym_app.core.configuration;

import com.gym_app.core.dto.Trainee;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.dto.Training;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.services.TraineeService;
import com.gym_app.core.services.TrainerService;
import com.gym_app.core.services.TrainingService;
import com.gym_app.core.util.ResourceFileReader;
import com.gym_app.core.util.TraineeFactory;
import com.gym_app.core.util.TrainerFactory;
import com.gym_app.core.util.TrainingFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

@Configuration
public class MapRepoStartupConfiguration {
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
    public void init() {
        addTrainees();
        addTrainers();
//        addTrainings();
    }

    public void addTrainers() {
        ArrayList<String> trainerData;
        try {
            trainerData = ResourceFileReader.readFile(trainerFilePath);
            for (String entry : trainerData) {
                String[] args = entry.split(",");
                Trainer trainer = trainerFactory.createUser(args[0], args[1], Boolean.parseBoolean(args[2]), args[3]);
                trainerService.create(trainer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addTrainees() {
        ArrayList<String> traineeData;
        try {
            traineeData = ResourceFileReader.readFile(traineeFilePath);
            for (String entry : traineeData) {
                String[] args = entry.split(",");
                Trainee trainee = traineeFactory.createUser(args[0], args[1], Boolean.parseBoolean(args[2]), args[3], args[4]);
                traineeService.create(trainee);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void addTrainings() {
        ArrayList<String> trainingsData;
        try {
            trainingsData = ResourceFileReader.readFile(trainingsFilePath);
            for (String entry : trainingsData) {
                String[] args = entry.split(",");
                Training training = trainingFactory.createService(Long.parseLong(args[0]), Long.parseLong(args[1]), args[2], LocalDate.parse(args[3]), Integer.parseInt(args[4]), TrainingType.valueOf(args[5]));
                trainingService.create(training);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
