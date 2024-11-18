package com.gym_app.core.services;

import com.gym_app.core.dao.TrainingJpaDao;
import com.gym_app.core.dto.common.Trainee;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.util.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TraineeDbServiceTest {

    @Autowired
    private TraineeDbService traineeDbService;
    @Autowired
    private TrainerDBService trainerDBService;
    @Autowired
    private TrainingJpaDao trainingJpaDao;

    private Trainee trainee;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        // Create a new Trainee instance
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        trainee = new Trainee();
        trainee.setFirstName("Alice");
        trainee.setLastName("Smith");
        trainee.setUserName("Alice.Smith");

        String password = PasswordGenerator.createPassword(10);
        String hashedPassword = passwordEncoder.encode(password);
        trainee.setPassword(hashedPassword);

        trainee.setActive(true);
        trainee.setDateOfBirth(LocalDate.parse("2004-01-03"));
        trainee.setAddress("Osaka");

        // Save the trainee to the database
        traineeDbService.getDao().save(trainee);
        trainer = trainerDBService.create(new Trainer("Red", "One", "Red.One", "password01", true, TrainingType.FITNESS));

    }

    @Test
    void create_ShouldSaveTrainee_WhenSuccessful() {
        Trainee newTrainee = new Trainee();
        newTrainee.setFirstName("Bob");
        newTrainee.setLastName("Brown");
        newTrainee.setActive(true);

        Trainee createdTrainee = traineeDbService.create(newTrainee);

        assertNotNull(createdTrainee);
        assertNotEquals(trainee.getId(), createdTrainee.getId());
        assertEquals("Bob.Brown", createdTrainee.getUserName());
        assertTrue(traineeDbService.getDao().getAll().contains(createdTrainee));
    }


    @Test
    void delete_ShouldRemoveTrainee_WhenAuthenticated() {
        assertTrue(traineeDbService.getDao().getAll().contains(trainee));
        assertDoesNotThrow(() -> traineeDbService.delete(trainee.getUserName()));
        assertFalse(traineeDbService.getDao().getAll().contains(trainee));
    }
    
    @Test
    void delete_ShouldRemoveRelevantTrainings() {
        Training training = traineeDbService.addTraining(
                trainee.getUserName(),
                trainer,
                "Test training1",
                TrainingType.YOGA,
                LocalDate.now().plusDays(2),
                60);
        assertTrue(traineeDbService.getDao().getAll().contains(trainee));


        assertDoesNotThrow(()-> traineeDbService.addTraining(trainee.getUserName(),
                trainer,
                trainer.getSpecialization() + " training",
                trainer.getSpecialization(),
                LocalDate.now().plusDays(2),
                60));
        assertDoesNotThrow(()-> traineeDbService.delete(trainee.getUserName()));
        assertFalse(traineeDbService.getDao().getAll().contains(trainee));
    }

    @Test
    void selectByUsername_ShouldReturnTrainee_WhenAuthenticated() {
        Optional<Trainee> foundTrainee = traineeDbService.selectByUsername(trainee.getUserName());

        assertTrue(foundTrainee.isPresent());
        assertEquals(trainee.getId(), foundTrainee.get().getId());
    }
    

    @Test
    void changePassword_ShouldUpdatePassword_WhenAuthenticated() {
        String newPassword = "newPassword123";
        traineeDbService.changePassword(newPassword, trainee.getUserName());

        Optional<Trainee> updatedTrainee = traineeDbService.selectByUsername(trainee.getUserName());
        assertTrue(updatedTrainee.isPresent());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertTrue(passwordEncoder.matches(newPassword, updatedTrainee.get().getPassword()));
    }
    

    @Test
    void changeStatus_ShouldToggleTraineeStatus_WhenAuthenticated() {
        boolean initialStatus = trainee.isActive();
        traineeDbService.changeStatus(trainee, trainee.getUserName());
        Optional<Trainee> updatedTrainee = traineeDbService.selectByUsername(trainee.getUserName());
        assertTrue(updatedTrainee.isPresent());
        assertNotEquals(initialStatus, updatedTrainee.get().isActive());
    }

    @Test
    void update_ShouldUpdateTrainee_WhenAuthenticated() {
        String[] updates = {"New FirstName", "New LastName", "NewUsername", "NewPassword", "true", "2024-05-05", "Tokio"}; // Modify as per Trainee fields
        traineeDbService.update(trainee, trainee.getUserName(), updates);

        Optional<Trainee> updatedTrainee = traineeDbService.selectByUsername(trainee.getUserName());
        assertTrue(updatedTrainee.isPresent());
        assertEquals("New FirstName", updatedTrainee.get().getFirstName());
        assertEquals("New LastName", updatedTrainee.get().getLastName());
        // Add more assertions for other updated fields as needed
    }


    @Test
    void getTraineeTrainings_Test() {

        Training training = traineeDbService.addTraining(
                trainee.getUserName(),
                trainer,
                trainer.getSpecialization() + " training",
                trainer.getSpecialization(),
                LocalDate.now().plusDays(4),
                60);
        List<Training> trainings = traineeDbService.getTraineeTrainings(
                trainee.getUserName(),
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                trainer.getUserName(),
                trainer.getSpecialization());
        System.out.println("Trainings: " + trainings);
        assertTrue(trainings.size() > 0);
    }

    @Test
    void getAvailableTrainers_Test() {
        List<Trainer> trainings = traineeDbService.getAvailableTrainers(
                trainee.getUserName());
        assertTrue(trainings.size() > 0);
    }

    @Test
    void addTraining_Test() {

        assertDoesNotThrow(() -> traineeDbService.addTraining(
                        trainee.getUserName(),
                        trainer,
                        "Test training1",
                        TrainingType.YOGA,
                        LocalDate.now().plusDays(2),
                        60),
                "Successful training save should not throw an exception");

        Training training2 = traineeDbService.addTraining(
                trainee.getUserName(),
                trainer,
                "Test training2",
                TrainingType.STRETCHING,
                LocalDate.now().plusDays(3),
                60);
    }


    @Test
    void testAddTrainerToList_Success() {
        String username = trainee.getUserName();

        assertDoesNotThrow(()->traineeDbService.addTrainerToList(username,trainer),
                "");

        trainee = traineeDbService.selectByUsername(username).get();
        assertTrue(trainee.getTrainers().contains(trainer));
    }

    @Test
    void testRemoveTrainerFromList_Success() {
        String username = trainee.getUserName();

        traineeDbService.addTrainerToList(username, trainer);
        assertTrue(traineeDbService.selectByUsername(username).get().getTrainers().contains(trainer),
                "");

        assertDoesNotThrow(()->traineeDbService.removeTrainerFromList(username,trainer),
                "");
        assertFalse(traineeDbService.getTraineesTrainers(username).contains(trainer));
    }


    @Test
    void testGetTraineesTrainers_Success() {
        String username = trainee.getUserName();

        traineeDbService.addTrainerToList(username, trainer);
        assertTrue(traineeDbService.selectByUsername(username).get().getTrainers().contains(trainer),
                "");
    }

}
