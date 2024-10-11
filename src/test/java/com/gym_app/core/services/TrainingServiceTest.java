package com.gym_app.core.services;

import com.gym_app.core.CoreApplication;
import com.gym_app.core.dto.Training;
import com.gym_app.core.enums.TrainingType;
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
        trainingRepository.getRepository().clear();

        trainings[0] = trainingsService.create(trainingFactory.createTraining(1L, 2L, "Sunday Stretching", TrainingType.STRETCHING, LocalDate.now().plusDays(2), 60));
        trainings[1] = trainingsService.create(trainingFactory.createTraining(2L, 3L, "Morning Yoga", TrainingType.YOGA, LocalDate.now().plusDays(3), 90));
        trainings[2] = trainingsService.create(trainingFactory.createTraining(3L, 1L, "Resistance Training", TrainingType.RESISTANCE, LocalDate.now().plusDays(4), 75));
        trainings[3] = trainingsService.create(trainingFactory.createTraining(4L, 5L, "Evening Zumba", TrainingType.ZUMBA, LocalDate.now().plusDays(1), 50));
        trainings[4] = trainingsService.create(trainingFactory.createTraining(5L, 4L, "Power Lifting", TrainingType.RESISTANCE, LocalDate.now().plusDays(5), 120));
        trainings[5] = trainingsService.create(trainingFactory.createTraining(6L, 6L, "Full Body Fitness", TrainingType.FITNESS, LocalDate.now().plusDays(6), 90));
    }

    @Test
    public void testCreateTrainings() {
        int expectedSize = 6;
        assertEquals(expectedSize, trainingRepository.getRepository().size(), "Repository size after creation should be " + expectedSize);

        Training additionalTraining = trainingFactory.createTraining(7L, 2L, "Cardio Blast", TrainingType.FITNESS, LocalDate.now().plusDays(7), 45);
        trainingsService.create(additionalTraining);
        assertEquals(++expectedSize, trainingRepository.getRepository().size(), "Repository size after creation should be" + (expectedSize+1));

        assertThrows(IllegalArgumentException.class, () -> trainingsService.create(null), "Creating a null training should throw an exception");
    }

    @Test
    public void testUpdateTraining() {
        long idSample = trainings[2].getServiceId();
        String expectedString = "Power Yoga";

        String[] updateInfo = new String[]{"8", "1", expectedString, "YOGA", "2024-10-18", "50"};

        Training sampleTraining = trainingRepository.getRepository().get(idSample);


        trainingsService.update(sampleTraining, updateInfo);

        assertEquals(expectedString, trainingsService.select(idSample).get().getServiceName(),
                "Training name should be " + expectedString);

        assertThrows(IllegalArgumentException.class, () -> trainingsService.update(null, updateInfo),
                "Updating a null training should throw an exception");

        assertThrows(IllegalArgumentException.class, () -> trainingsService.update(sampleTraining, null),
                "Updating with a null updates array should throw an exception");

        assertThrows(IllegalArgumentException.class, () -> trainingsService.update(sampleTraining, new String[]{}),
                "Updating with an empty updates array should throw an exception");
    }

    @Test
    public void testDeleteTraining() {
        long idSample = trainings[3].getServiceId();
        int expectedSize = 5;
        trainingsService.delete(idSample);

        assertThrows(NoSuchElementException.class, () -> trainingsService.delete(idSample),
                "Deleting a non-existing training should throw NoSuchElementException");

        assertEquals(expectedSize, trainingRepository.getRepository().size(),
                "Repository size after deletion should be 4");
    }

    @Test
    public void testSelectTraining() {
        long idSample = trainings[4].getServiceId();
        Optional<Training> selectedTraining = trainingsService.select(idSample);

        assertTrue(selectedTraining.isPresent(), "Training with ID " + idSample + " should be present");

        assertThrows(NoSuchElementException.class, () -> trainingsService.select(999L),
                "Selecting a non-existing training should throw an exception");
    }
}
