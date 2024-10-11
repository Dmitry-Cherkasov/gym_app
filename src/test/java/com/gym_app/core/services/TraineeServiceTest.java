package com.gym_app.core.services;

import com.gym_app.core.CoreApplication;
import com.gym_app.core.dto.Trainee;
import com.gym_app.core.repo.TraineeRepository;
import com.gym_app.core.util.TraineeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreApplication.class)
public class TraineeServiceTest {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    TraineeFactory traineeFactory;

    private Trainee[] trainees;

    // Prepare trainees before each test
    @BeforeEach
    public void setup() {
        trainees = new Trainee[7];
        traineeRepository.getRepository().clear();

        trainees[0] = traineeService.create(new Trainee("John", "Wick", "John.Wick", "asasdasdad", true, LocalDate.now().minusYears(20), "Hotel Continental"));
        trainees[1] = traineeService.create(new Trainee("Alice", "Smith", "Alice.Smith", "password123", false, LocalDate.now().minusYears(21), null));
        trainees[2] = traineeService.create(new Trainee("Bruce", "Wayne", "Bruce.Wayne", "batman123", true, LocalDate.now().minusYears(22), "Bat-cave"));
        trainees[3] = traineeService.create(new Trainee("Diana", "Prince", "Diana.Prince", "wonderwoman", true, LocalDate.now().minusYears(23), "Greece"));
        trainees[4] = traineeService.create(new Trainee("Clark", "Kent", "Clark.Kent", "superman007", true, LocalDate.now().minusYears(24), "Krypton"));
    }

        @Test
        public void testCreateTrainees () {
            assertEquals(5, traineeRepository.getRepository().size(), "Repository size after creation should be 5");

            Trainee additionalTrainee = traineeFactory.createTrainee("Tony", "Stark", true, LocalDate.now().minusYears(50), "RIP");
            traineeService.create(additionalTrainee);
            assertEquals(6, traineeRepository.getRepository().size(), "Repository size after creation should be 6");

            traineeService.create(traineeFactory.createTrainee("Tony", "Stark", true, LocalDate.now().minusYears(50), "RIP"));
            traineeService.create(traineeFactory.createTrainee("Tony", "Stark", true, LocalDate.now().minusYears(50), "RIP"));
            assertEquals(8, traineeRepository.getRepository().size(), "Repository size after creation should be 6");
            assertTrue(traineeRepository.getRepository().containsKey("Tony.Stark1"));
            assertTrue(traineeRepository.getRepository().containsKey("Tony.Stark2"));

            System.out.println(traineeRepository.getRepository());
            assertThrows(IllegalArgumentException.class, () -> traineeService.create(null), "Creating a null trainee should throw an exception");
        }

        @Test
        public void testUpdateTrainee () {
            String idSample = trainees[2].getUserName();
            String[] updateInfo = new String[]{"Bruce", "Lee", "Bruce.Lee", "ipmansson1970", "false", "1940-11-27", "New York"};
            Trainee sampleTrainee = traineeRepository.getRepository().get(idSample);
            String expectedString = "Lee";

            traineeService.update(sampleTrainee, updateInfo);

            assertEquals(expectedString, traineeService.select(idSample).get().getLastName(),
                    "Trainee last name should be " + expectedString);

            assertThrows(IllegalArgumentException.class, () -> traineeService.update(null, updateInfo),
                    "Updating a null trainee should throw an exception");

            assertThrows(IllegalArgumentException.class, () -> traineeService.update(sampleTrainee, null),
                    "Updating with a null updates array should throw an exception");

            assertThrows(IllegalArgumentException.class, () -> traineeService.update(sampleTrainee, new String[]{}),
                    "Updating with an empty updates array should throw an exception");
        }

        @Test
        public void testDeleteTrainee () {
            String idSample = trainees[3].getUserName();
            traineeService.delete(idSample);

            assertThrows(NoSuchElementException.class, () -> traineeService.delete(idSample),
                    "Deleting a non-existing trainee should throw NoSuchElementException");

            assertEquals(4, traineeRepository.getRepository().size(),
                    "Repository size after deletion should be 4");
        }

        @Test
        public void testSelectTrainee () {
            String idSample = trainees[4].getUserName();
            Optional<Trainee> selectedTrainee = traineeService.select(idSample);

            assertTrue(selectedTrainee.isPresent(), "Trainee with UserName " + idSample + " should be present");

            assertThrows(NoSuchElementException.class, () -> traineeService.select("samantha.ahtnamas"),
                    "Selecting a non-existing trainee should throw an exception");
        }
    }
