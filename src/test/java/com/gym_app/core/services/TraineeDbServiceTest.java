package com.gym_app.core.services;

import com.gym_app.core.dao.TrainerJpaDaoImpl;
import com.gym_app.core.dao.TrainingJpaDao;
import com.gym_app.core.dto.Trainee;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.dto.Training;
import com.gym_app.core.enums.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    private TrainerJpaDaoImpl trainerJpaDao;
    @Autowired
    private TrainingJpaDao trainingJpaDao;

    private Trainee trainee;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        // Create a new Trainee instance
        trainee = new Trainee();
        trainee.setFirstName("Alice");
        trainee.setLastName("Smith");
        trainee.setActive(true);
        trainee.setDateOfBirth(LocalDate.parse("2004-01-03"));
        trainee.setAddress("Osaka");
        // Set any other relevant fields for Trainee
        trainee = traineeDbService.create(trainee); // Save the trainee to the database
        trainer = trainerJpaDao.save(new Trainer("Red", "One", "Red.One", "password01", true, TrainingType.FITNESS));

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
    }


    @Test
    void delete_ShouldRemoveTrainee_WhenAuthenticated() {
        assertTrue(traineeDbService.getDao().getAll().contains(trainee));
        assertDoesNotThrow(() -> traineeDbService.delete(trainee.getUserName(), trainee.getPassword()));
        assertFalse(traineeDbService.getDao().getAll().contains(trainee));
    }

    @Test
    void delete_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> traineeDbService.delete(trainee.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainee with username: " + trainee.getUserName(), exception.getMessage());
    }

    @Test
    void delete_ShouldRemoveRelevantTrainings() {
        Training training = traineeDbService.addTraining(
                trainee.getUserName(),
                trainee.getPassword(),
                trainer,
                "Test training1",
                TrainingType.YOGA,
                LocalDate.now().plusDays(2),
                60);
        assertTrue(traineeDbService.getDao().getAll().contains(trainee));


        assertDoesNotThrow(()-> traineeDbService.addTraining(trainee.getUserName(),
                trainee.getPassword(),
                trainer,
                trainer.getSpecialization() + " training",
                trainer.getSpecialization(),
                LocalDate.now().plusDays(2),
                60));
        assertDoesNotThrow(()-> traineeDbService.delete(trainee.getUserName(), trainee.getPassword()));
        assertFalse(traineeDbService.getDao().getAll().contains(trainee));
    }

    @Test
    void selectByUsername_ShouldReturnTrainee_WhenAuthenticated() {
        Optional<Trainee> foundTrainee = traineeDbService.selectByUsername(trainee.getUserName(), trainee.getPassword(), trainee.getUserName());

        assertTrue(foundTrainee.isPresent());
        assertEquals(trainee.getId(), foundTrainee.get().getId());
    }

    @Test
    void selectByUsername_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> traineeDbService.selectByUsername(trainee.getUserName(), "wrongPassword", trainee.getUserName()));

        assertEquals("Authentication failed for trainee with username: Alice.Smith", exception.getMessage());
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenAuthenticated() {
        String newPassword = "newPassword123";
        traineeDbService.changePassword(newPassword, trainee.getUserName(), trainee.getPassword());

        Optional<Trainee> updatedTrainee = traineeDbService.selectByUsername(trainee.getUserName(), trainee.getPassword(), trainee.getUserName());
        assertTrue(updatedTrainee.isPresent());
        assertEquals(newPassword, updatedTrainee.get().getPassword()); // Ensure to verify the password hash if using hashing
    }

    @Test
    void changePassword_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> traineeDbService.changePassword("newPassword123", trainee.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainee with username: " + trainee.getUserName(), exception.getMessage());
    }

    @Test
    void changeStatus_ShouldToggleTraineeStatus_WhenAuthenticated() {
        boolean initialStatus = trainee.isActive();
        traineeDbService.changeStatus(trainee, trainee.getUserName(), trainee.getPassword());
        Optional<Trainee> updatedTrainee = traineeDbService.selectByUsername(trainee.getUserName(), trainee.getPassword(), trainee.getUserName());
        assertTrue(updatedTrainee.isPresent());
        assertNotEquals(initialStatus, updatedTrainee.get().isActive());
    }

    @Test
    void changeStatus_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> traineeDbService.changeStatus(trainee, trainee.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainee with username: " + trainee.getUserName(), exception.getMessage());
    }

    @Test
    void update_ShouldUpdateTrainee_WhenAuthenticated() {
        String[] updates = {"New FirstName", "New LastName", "NewUsername", "NewPassword", "true", "2024-05-05", "Tokio"}; // Modify as per Trainee fields
        traineeDbService.update(trainee, trainee.getUserName(), trainee.getPassword(), updates);

        Optional<Trainee> updatedTrainee = traineeDbService.selectByUsername(trainee.getUserName(), trainee.getPassword(), trainee.getUserName());
        assertTrue(updatedTrainee.isPresent());
        assertEquals("New FirstName", updatedTrainee.get().getFirstName());
        assertEquals("New LastName", updatedTrainee.get().getLastName());
        // Add more assertions for other updated fields as needed
    }

    @Test
    void update_ShouldThrowException_WhenAuthenticationFails() {
        String[] updates = {"New FirstName", "New LastName", "NEW_SPECIALIZATION"};
        SecurityException exception = assertThrows(SecurityException.class,
                () -> traineeDbService.update(trainee, trainee.getUserName(), "wrongPassword", updates));

        assertEquals("Authentication failed for trainee with username: " + trainee.getUserName(), exception.getMessage());
    }

    @Test
    void getTraineeTrainings_Test() {
        assertThrows(SecurityException.class, () -> traineeDbService.getTraineeTrainings(
                        trainee.getUserName(),
                        "wrongPassword",
                        LocalDate.now(),
                        LocalDate.now().plusDays(10),
                        "Michael.Johnson",
                        TrainingType.ZUMBA),
                "Failed authentication should throw exception");

        Training training = traineeDbService.addTraining(
                trainee.getUserName(),
                trainee.getPassword(),
                trainer,
                trainer.getSpecialization() + " training",
                trainer.getSpecialization(),
                LocalDate.now().plusDays(4),
                60);
        List<Training> trainings = traineeDbService.getTraineeTrainings(
                trainee.getUserName(),
                trainee.getPassword(),
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
                trainee.getUserName(),
                trainee.getPassword());
        assertTrue(trainings.size() > 0);

        assertThrows(SecurityException.class, () -> traineeDbService.getAvailableTrainers(
                        "Brian.Lee",
                        "wrongPassword"),
                "Failed authentication should throw exception");
    }

    @Test
    void addTraining_Test() {

        assertDoesNotThrow(() -> traineeDbService.addTraining(
                        trainee.getUserName(),
                        trainee.getPassword(),
                        trainer,
                        "Test training1",
                        TrainingType.YOGA,
                        LocalDate.now().plusDays(2),
                        60),
                "Successful training save should not throw an exception");

        Training training2 = traineeDbService.addTraining(
                trainee.getUserName(),
                trainee.getPassword(),
                trainer,
                "Test training2",
                TrainingType.STRETCHING,
                LocalDate.now().plusDays(3),
                60);

        assertThrows(SecurityException.class, () -> traineeDbService.addTraining(
                        trainee.getUserName(),
                        "WrongPass",
                        trainer,
                        "Test training",
                        TrainingType.YOGA,
                        LocalDate.now().plusDays(2),
                        60),
                "Authentication fail should throw an exception");
    }


    @Test
    void testAddTrainerToList_Success() {
        String username = trainee.getUserName();
        String password = trainee.getPassword();

        assertDoesNotThrow(()->traineeDbService.addTrainerToList(username,password,trainer),
                "");

        trainee = traineeDbService.selectByUsername(username,password,username).get();
        assertTrue(trainee.getTrainers().contains(trainer));
    }

    @Test
    void testAddTrainerToList_AuthenticationFailed() {
        String username = trainee.getUserName();
        String password = "wrong_password";

        assertThrows(SecurityException.class, ()->traineeDbService.addTrainerToList(username,password,trainer),"");
    }

    @Test
    void testRemoveTrainerFromList_Success() {
        String username = trainee.getUserName();
        String password = trainee.getPassword();

        traineeDbService.addTrainerToList(username,password, trainer);
        assertTrue(traineeDbService.selectByUsername(username,password,username).get().getTrainers().contains(trainer),
                "");

        assertDoesNotThrow(()->traineeDbService.removeTrainerFromList(username,password,trainer),
                "");
        assertFalse(traineeDbService.getTraineesTrainers(username,password).contains(trainer));
    }


    @Test
    void testGetTraineesTrainers_Success() {
        String username = trainee.getUserName();
        String password = trainee.getPassword();

        traineeDbService.addTrainerToList(username,password, trainer);
        assertTrue(traineeDbService.selectByUsername(username,password,username).get().getTrainers().contains(trainer),
                "");
    }

}
