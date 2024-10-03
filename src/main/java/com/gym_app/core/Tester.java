package com.gym_app.core;

import com.gym_app.core.dto.Trainee;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.dto.Training;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.util.*;

import java.time.LocalDate;

public class Tester {

    public static void main(String[] args) {

        CreatePolicy cp = new StandardCreatePolicy();
        TrainerFactory trainerFactory = new TrainerFactory(cp);
        TraineeFactory traineeFactory = new TraineeFactory(cp);
        Trainer trainer = trainerFactory.createTrainer("Susan", "Musan", true, TrainingType.FITNESS);
        System.out.println(trainer);
        Trainee trainee = traineeFactory.createTrainee("Bob", "Rob", false, LocalDate.now(), "Somwhere");
        System.out.println(trainee);

        TrainingFactory trainingFactory = new TrainingFactory();
        Training training = trainingFactory.createTraining(trainee, trainer, "Sunday training", TrainingType.YOGA, LocalDate.now().plusDays(2), 60);
        System.out.println(training);


    }

}
