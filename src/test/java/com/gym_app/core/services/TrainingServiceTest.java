package com.gym_app.core.services;

import com.gym_app.core.CoreApplication;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.repo.TrainingRepository;
import com.gym_app.core.util.TrainingFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreApplication.class)
public class TrainingServiceTest {

    @Autowired
    private TrainingService trainingsService;

    @Autowired
    private TrainingRepository trainingRepository;
    
    @Autowired
    TrainingFactory trainingFactory;


    private Training[] trainings;

    // Prepare trainings before each test
    @BeforeEach
    public void setup() {
        trainings = new Training[6];
        trainingRepository.clear();

        trainings[0] = trainingsService.create(trainingFactory.createService(1L, 14L, "Sunday Stretching", LocalDate.now().plusDays(2), 60, "STRETCHING"));
        trainings[1] = trainingsService.create(trainingFactory.createService(2L, 27L, "Morning Yoga", LocalDate.now().plusDays(3), 90, "YOGA"));
        trainings[2] = trainingsService.create(trainingFactory.createService(1L, 27L, "Resistance Training",  LocalDate.now().plusDays(4), 75, "RESISTANCE"));
        trainings[3] = trainingsService.create(trainingFactory.createService(12L, 10L, "Evening Zumba", LocalDate.now().plusDays(1), 50, "ZUMBA"));
        trainings[4] = trainingsService.create(trainingFactory.createService(6L, 14L, "Power Lifting", LocalDate.now().plusDays(5), 120, "RESISTANCE"));
        trainings[5] = trainingsService.create(trainingFactory.createService(21L, 10L, "Full Body Fitness", LocalDate.now().plusDays(6), 90, "FITNESS"));
    }

    @Test
    public void testCreateTrainings() {
        int expectedSize = 6;
        assertEquals(expectedSize, trainingRepository.size(), "Repository size after creation should be " + expectedSize);

        Training additionalTraining = trainingFactory.createService(6L, 14L, "Cardio Blast", LocalDate.now().plusDays(7), 45, "FITNESS");
        trainingsService.create(additionalTraining);
        assertEquals(++expectedSize, trainingRepository.size(), "Repository size after creation should be" + (expectedSize+1));

        assertThrows(IllegalArgumentException.class, () -> trainingsService.create(null), "Creating a null training should throw an exception");
    }

    @Test
    public void testUpdateTraining() {
        long idSample = trainings[2].getTrainingId();
        String expectedString = "Power Yoga";

        String[] updateInfo = new String[]{"1", "10", expectedString, "YOGA", "2024-10-18", "50"};

        Training sampleTraining = trainingRepository.get(idSample);


        trainingsService.update(sampleTraining, updateInfo);

        assertEquals(expectedString, trainingsService.select(idSample).get().getTrainingName(),
                "Training name should be " + expectedString);

        assertThrows(RuntimeException.class, () -> trainingsService.update(null, updateInfo),
                "Updating a null training should throw an exception");

        assertThrows(RuntimeException.class, () -> trainingsService.update(sampleTraining, null),
                "Updating with a null updates array should throw an exception");

        assertThrows(RuntimeException.class, () -> trainingsService.update(sampleTraining, new String[]{}),
                "Updating with an empty updates array should throw an exception");
    }

    @Test
    public void testDeleteTraining() {
        long idSample = trainings[3].getTrainingId();
        int expectedSize = 5;
        trainingsService.delete(idSample);

        assertThrows(NoSuchElementException.class, () -> trainingsService.delete(idSample),
                "Deleting a non-existing training should throw NoSuchElementException");

        assertEquals(expectedSize, trainingRepository.size(),
                "Repository size after deletion should be 4");
    }

    @Test
    public void testSelectTraining() {
        long idSample = trainings[4].getTrainingId();
        Optional<Training> selectedTraining = trainingsService.select(idSample);

        assertTrue(selectedTraining.isPresent(), "Training with ID " + idSample + " should be present");

        assertThrows(NoSuchElementException.class, () -> trainingsService.select(999L),
                "Selecting a non-existing training should throw an exception");
    }
}
