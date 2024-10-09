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

    // Prepare trainers before each test
    @BeforeEach
    public void setup() {
        trainers = new Trainer[5];
        trainerRepository.getRepository().clear();

        trainers[0] =  trainerService.create(new Trainer("John", "Wick", "John.Wick", "asasdasdad", true, TrainingType.YOGA));
        trainers[1] =  trainerService.create(new Trainer("Alice", "Smith", "Alice.Smith", "password123", false, TrainingType.FITNESS));
        trainers[2] =  trainerService.create(new Trainer("Bruce", "Wayne", "Bruce.Wayne", "batman123", true, TrainingType.RESISTANCE));
        trainers[3] =  trainerService.create(new Trainer("Diana", "Prince", "Diana.Prince", "wonderwoman", true, TrainingType.STRETCHING));
        trainers[4] =  trainerService.create(new Trainer("Clark", "Kent", "Clark.Kent", "superman007", true, TrainingType.YOGA));
        System.out.println(Arrays.toString(trainers));
    }

    @Test
    public void testCreateTrainers() {
        System.out.println(trainerRepository.getRepository());
        assertEquals(5, trainerRepository.getRepository().size(), "Repository size after creation should be 5");

        Trainer additonalTrainer = new Trainer("John", "Bon Jovy", "John.Bon Jovy", "neworleans1990", false, TrainingType.ZUMBA);
        trainerService.create(additonalTrainer);
        assertEquals(6, trainerRepository.getRepository().size(), "Repository size after creation should be 6");

        assertThrows(IllegalArgumentException.class, () -> trainerService.create(null), "Creating a null trainer should return false");
    }

    @Test
    public void testUpdateTrainer() {
        long idSample = trainers[2].getUserId();
        String[] updateInfo = new String[]{"Bruce", "Lee", "Bruce.Lee", "kungfupanda", "true", "RESISTANCE"};
        Trainer sampleTrainer = trainerRepository.getRepository().get(idSample);
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
        long idSample = trainers[3].getUserId();
        trainerService.delete(idSample);

        assertThrows(NoSuchElementException.class, () -> trainerService.delete(idSample),
                "Selecting a deleted trainer should throw NoSuchElementException");

        assertEquals(4, trainerRepository.getRepository().size(),
                "Repository size after deletion should be 4");

    }

    @Test
    public void testSelectTrainer() {
        long idSample = trainers[4].getUserId();
        Optional<Trainer> selectedTrainer = trainerService.select(idSample);

        assertTrue(selectedTrainer.isPresent(), "Trainer with ID " + idSample + " should be present");

        assertThrows(NoSuchElementException.class, () -> trainerService.select(999L), "Selecting a non-existing trainer should throw an exception");
    }
}
