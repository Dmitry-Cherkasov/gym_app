package com.gym_app.core.services;

import com.gym_app.core.CoreApplication;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.repo.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreApplication.class)
public class TrainerServiceTest {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainerRepository trainerRepository;

    private Trainer[] trainers;
    private long idSample;

    // Prepare trainers before each test
    @BeforeEach
    public void setup() {
        trainers = new Trainer[5];
        trainers[0] = new Trainer("John", "Wick", "John.Wick", "asasdasdad", true, TrainingType.YOGA);
        trainers[1] = new Trainer("Alice", "Smith", "Alice.Smith", "password123", false, TrainingType.FITNESS);
        trainers[2] = new Trainer("Bruce", "Wayne", "Bruce.Wayne", "batman123", true, TrainingType.RESISTANCE);
        trainers[3] = new Trainer("Diana", "Prince", "Diana.Prince", "wonderwoman", true, TrainingType.STRETCHING);
        trainers[4] = new Trainer("Clark", "Kent", "Clark.Kent", "superman007", true, TrainingType.YOGA);

        // Clear any existing data to avoid test interference
        trainerRepository.getTrainerRepository().clear();
        Arrays.stream(trainers).forEach(trainer -> trainerService.create(trainer));
        idSample = trainerRepository.getTrainerRepository().entrySet().stream().skip(1).findFirst().get().getValue().getUserId();
    }

    @Test
    public void testCreateTrainers() {
        assertEquals(5, trainerRepository.getTrainerRepository().size(), "Repository size after creation should be 5");

        Trainer additonalTrainer = new Trainer("John", "Bon Jovy", "John.Bon Jovy", "neworleans1990", false, TrainingType.ZUMBA);
        trainerService.create(additonalTrainer);
        assertEquals(6, trainerRepository.getTrainerRepository().size(), "Repository size after creation should be 6");

        assertThrows(IllegalArgumentException.class, () -> trainerService.create(null), "Creating a null trainer should return false");
    }

    @Test
    public void testUpdateTrainer() {

        String[] updateInfo = new String[]{"Bruce", "Lee", "Bruce.Lee", "kungfupanda", "true", "RESISTANCE"};
        Trainer sampleTrainer = trainerRepository.getTrainerRepository().get(idSample);
        String expectedString = "Lee";

        trainerService.update(sampleTrainer, updateInfo);
        assertEquals(expectedString, trainerService.select(idSample).get().getLastName(),
                "Trainer last name should be" + expectedString);

        assertThrows(IllegalArgumentException.class, () -> trainerService.update(null, updateInfo),
                "Selecting a non-existing trainer should throw an exception");

        assertThrows(IllegalArgumentException.class, () -> trainerService.update(sampleTrainer, null),
                "Selecting a non-existing updates array should throw an exception");

        assertThrows(IllegalArgumentException.class, () -> trainerService.update(sampleTrainer, new String[]{}),
                "Selecting empty updates array should throw an exception");

    }

    @Test
    public void testDeleteTrainer() {
        trainerService.delete(idSample);

        assertThrows(NoSuchElementException.class, () -> trainerService.delete(idSample),
                "Selecting a deleted trainer should throw NoSuchElementException");

        assertEquals(4, trainerRepository.getTrainerRepository().size(),
                "Repository size after deletion should be 4");

    }

    @Test
    public void testSelectTrainer() {

        Optional<Trainer> selectedTrainer = trainerService.select(idSample);

        assertTrue(selectedTrainer.isPresent(), "Trainer with ID " + idSample + " should be present");

        assertThrows(NoSuchElementException.class, () -> trainerService.select(999L), "Selecting a non-existing trainer should throw an exception");
    }
}
