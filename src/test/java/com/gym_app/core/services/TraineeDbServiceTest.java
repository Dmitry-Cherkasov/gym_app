package com.gym_app.core.services;

import com.gym_app.core.dao.TraineeJpaDaoImpl;
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
    private TraineeJpaDaoImpl traineeJpaDao;

    private Trainee trainee;

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
        assertDoesNotThrow(() -> traineeDbService.delete(trainee.getId(), trainee.getUserName(), trainee.getPassword()));

        Optional<Trainee> deletedTrainee = traineeJpaDao.getById(trainee.getId());
        assertFalse(deletedTrainee.isPresent());
    }

    @Test
    void delete_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> traineeDbService.delete(trainee.getId(), trainee.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainee with username: Alice.Smith", exception.getMessage());
    }

    @Test
    void selectByUsername_ShouldReturnTrainee_WhenAuthenticated() {
        Optional<Trainee> foundTrainee = traineeDbService.selectByUsername("Alice.Smith", trainee.getPassword());

        assertTrue(foundTrainee.isPresent());
        assertEquals(trainee.getId(), foundTrainee.get().getId());
    }

    @Test
    void selectByUsername_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> traineeDbService.selectByUsername("Alice.Smith", "wrongPassword"));

        assertEquals("Authentication failed for trainee with username: Alice.Smith", exception.getMessage());
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenAuthenticated() {
        String newPassword = "newPassword123";
        traineeDbService.changePassword(newPassword, trainee.getUserName(), trainee.getPassword());

        Optional<Trainee> updatedTrainee = traineeJpaDao.getByUserName(trainee.getUserName());
        assertTrue(updatedTrainee.isPresent());
        assertEquals(newPassword, updatedTrainee.get().getPassword()); // Ensure to verify the password hash if using hashing
    }

    @Test
    void changePassword_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> traineeDbService.changePassword("newPassword123", trainee.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainee with username: Alice.Smith", exception.getMessage());
    }

    @Test
    void changeStatus_ShouldToggleTraineeStatus_WhenAuthenticated() {
        boolean initialStatus = trainee.isActive();
        traineeDbService.changeStatus(trainee, trainee.getUserName(), trainee.getPassword());
        Optional<Trainee> updatedTrainee = traineeJpaDao.getByUserName(trainee.getUserName());
        assertTrue(updatedTrainee.isPresent());
        assertNotEquals(initialStatus, updatedTrainee.get().isActive());
    }

    @Test
    void changeStatus_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> traineeDbService.changeStatus(trainee, trainee.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainee with username: Alice.Smith", exception.getMessage());
    }

    @Test
    void update_ShouldUpdateTrainee_WhenAuthenticated() {
        String[] updates = {"New FirstName", "New LastName", "NewUsername", "NewPassword", "true", "2024-05-05", "Tokio"}; // Modify as per Trainee fields
        traineeDbService.update(trainee, trainee.getUserName(), trainee.getPassword(), updates);

        Optional<Trainee> updatedTrainee = traineeJpaDao.getByUserName(trainee.getUserName());
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

        assertEquals("Authentication failed for trainee with username: Alice.Smith", exception.getMessage());
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

        List<Training> trainings = traineeDbService.getTraineeTrainings(
                "Grace.Hall",
                "wQ7TfheQnV",
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                "John.Smith",
                TrainingType.RESISTANCE);
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
                        "Test training1",
                        TrainingType.YOGA,
                        LocalDate.now().plusDays(2),
                        60),
                "Successful training save should not throw an exception");

        Training training2 = traineeDbService.addTraining(
                trainee.getUserName(),
                trainee.getPassword(),
                "Test training2",
                TrainingType.STRETCHING,
                LocalDate.now().plusDays(3),
                60);

        assertThrows(SecurityException.class, () -> traineeDbService.addTraining(
                        trainee.getUserName(),
                        "WrongPass",
                        "Test training",
                        TrainingType.YOGA,
                        LocalDate.now().plusDays(2),
                        60),
                "Authentication fail should throw an exception");
    }


}
