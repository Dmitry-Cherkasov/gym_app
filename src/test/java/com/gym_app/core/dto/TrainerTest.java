package com.gym_app.core.dto;

import com.gym_app.core.dto.common.Trainee;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.dto.common.User;
import com.gym_app.core.enums.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainerTest {

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer("John", "Doe", "johndoe", "password123", true, TrainingType.YOGA);
    }

    @Test
    void testTrainerConstructorAndGetters() {
        assertEquals("John", trainer.getFirstName());
        assertEquals("Doe", trainer.getLastName());
        assertEquals("johndoe", trainer.getUserName());
        assertEquals("password123", trainer.getPassword());
        assertTrue(trainer.isActive());
        assertEquals(TrainingType.YOGA, trainer.getSpecialization());
        assertTrue(trainer.getTrainings().isEmpty());
        assertTrue(trainer.getTrainees().isEmpty());
    }

    @Test
    void testSetAndGetSpecialization() {
        trainer.setSpecialization(TrainingType.FITNESS);
        assertEquals(TrainingType.FITNESS, trainer.getSpecialization());
    }

    @Test
    void testSetAndGetTrainings() {
        List<Training> trainings = new ArrayList<>();
        Training training = new Training();
        training.setTrainingName("Morning Session");
        trainings.add(training);

        trainer.setTrainings(trainings);
        assertEquals(trainings, trainer.getTrainings());
    }

    @Test
    void testSetAndGetTrainees() {
        List<Trainee> trainees = new ArrayList<>();
        Trainee trainee = new Trainee("Jane", "Doe", "janedoe", "password456", true, null, "123 Street");
        trainees.add(trainee);

        trainer.setTrainees(trainees);
        assertEquals(trainees, trainer.getTrainees());
    }

    @Test
    void testAddTrainingToTrainer() {
        Training training = new Training();
        training.setTrainingName("Evening Session");
        trainer.getTrainings().add(training);

        assertEquals(1, trainer.getTrainings().size());
        assertEquals(training, trainer.getTrainings().get(0));
    }


    @Test
    void testSetAndGetUser() {
        User user = new User("Jane", "Smith", "janesmith", "password789", true);
        trainer.setUser(user);

        assertEquals(user, trainer.getUser());
    }
}
