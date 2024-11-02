package com.gym_app.core.dto;

import com.gym_app.core.dto.common.Trainee;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.util.TrainingFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;


class TraineeTest {

    private Trainee trainee;
    private Trainer trainer;
    private Training training;
    private TrainingFactory trainingFactory = new TrainingFactory();

    @BeforeEach
    void setUp() {
        trainee = new Trainee("John", "Doe", "johndoe", "password123", true, LocalDate.of(1990, 5, 20), "123 Street");
        trainer = new Trainer("Jane", "Smith", "janesmith", "password456", true, TrainingType.YOGA);
        training = trainingFactory.createTraining(trainee,trainer);

        // Adding initial relationships
        trainee.addTrainer(trainer);
        trainee.getTrainings().add(training);
    }

    @Test
    void testTraineeFields() {
        assertEquals("John", trainee.getFirstName());
        assertEquals("Doe", trainee.getLastName());
        assertEquals("johndoe", trainee.getUserName());
        assertEquals("123 Street", trainee.getAddress());
        assertEquals(LocalDate.of(1990, 5, 20), trainee.getDateOfBirth());
    }

    @Test
    void testAddTrainer() {
        assertTrue(trainee.getTrainers().contains(trainer));
        assertTrue(trainer.getTrainees().contains(trainee));
    }

    @Test
    void testRemoveTrainer() {
        trainee.removeTrainer(trainer);
        assertFalse(trainee.getTrainers().contains(trainer));
        assertFalse(trainer.getTrainees().contains(trainee));
    }

    @Test
    void testAddTraining() {
        assertEquals(1, trainee.getTrainings().size());
        assertTrue(trainee.getTrainings().contains(training));
    }

    @Test
    void testCascadeTrainingRemoval() {
        // Assuming JPA/Hibernate setup is done, delete the trainee and verify cascade
        trainee.getTrainings().remove(training);
        assertEquals(0, trainee.getTrainings().size());
    }
}
