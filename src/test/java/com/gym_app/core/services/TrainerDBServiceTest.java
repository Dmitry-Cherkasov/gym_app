package com.gym_app.core.services;

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
class TrainerDBServiceTest {

    @Autowired
    private TrainerDBService trainerDBService;

    @Autowired
    private TraineeDbService traineeDbService;

    private Trainer trainer;
    private Trainee trainee;
    private Training training;
    private String password;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUserName("john.doe");

        password = PasswordGenerator.createPassword(10);
        trainer.setPassword(PasswordGenerator.hashPassword(password));

        trainer.setActive(true);
        trainer.setSpecialization(TrainingType.ZUMBA);
        trainer = trainerDBService.getDao().save(trainer);

        trainee = new Trainee("Test", "Trainee", "Test.Trainee", "", true, LocalDate.now().minusYears(25), "Imagination");
        String traineePass = PasswordGenerator.createPassword(10);
        trainee.setPassword(PasswordGenerator.hashPassword(traineePass));
        trainee = traineeDbService.getDao().save(trainee);

        training = traineeDbService.addTraining(
                trainee.getUserName(),
                traineePass,
                trainer,
                trainer.getSpecialization() + " training",
                trainer.getSpecialization(),
                LocalDate.now().plusDays(2),
                60);
    }

    @Test
    void create_ShouldSaveTrainer_WhenSuccessful() {
        Trainer newTrainer = new Trainer();
        newTrainer.setFirstName("Jane");
        newTrainer.setLastName("Doe");
        newTrainer.setActive(true);
        newTrainer.setSpecialization(TrainingType.FITNESS);

        Trainer createdTrainer = trainerDBService.create(newTrainer);

        assertNotNull(createdTrainer);
        assertNotEquals(trainer.getId(), createdTrainer.getId());
        assertEquals("Jane.Doe", createdTrainer.getUserName());
    }

    @Test
    void create_ShouldThrowException_WhenFieldsAreNull() {
        Trainer newTrainer = new Trainer(null, null, null, null, false, null); // All fields null

        assertThrows(Exception.class, () -> trainerDBService.create(newTrainer));
    }

    @Test
    void delete_ShouldRemoveTrainer_WhenAuthenticated() {
        assertDoesNotThrow(() -> trainerDBService.delete(trainer.getUserName(), password));
    }

    @Test
    void delete_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> trainerDBService.delete(trainer.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainer with username: " + trainer.getUserName(), exception.getMessage());
    }

    @Test
    void selectByUsername_ShouldReturnTrainer_WhenAuthenticated() {
        Optional<Trainer> foundTrainer = trainerDBService.selectByUsername(trainer.getUserName(), password);

        assertTrue(foundTrainer.isPresent());
        assertEquals(trainer.getId(), foundTrainer.get().getId());
    }

    @Test
    void selectByUsername_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> trainerDBService.selectByUsername(trainer.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainer with username: " + trainer.getUserName(), exception.getMessage());
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenAuthenticated() {
        String newPassword = "newPassword123";
        trainerDBService.changePassword(newPassword, trainer.getUserName(), password);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Optional<Trainer> updatedTrainer = trainerDBService.selectByUsername(trainer.getUserName(), newPassword);
        assertTrue(updatedTrainer.isPresent());
        assertTrue(passwordEncoder.matches(newPassword, updatedTrainer.get().getPassword())); // Ensure to verify the password hash if using hashing
    }

    @Test
    void changePassword_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> trainerDBService.changePassword("newPassword123", trainer.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainer with username: " + trainer.getUserName(), exception.getMessage());
    }

    @Test
    void changeStatus_ShouldToggleTrainerStatus_WhenAuthenticated() {
        boolean initialStatus = trainer.isActive();
        trainerDBService.changeStatus(trainer, trainer.getUserName(), password);
        Optional<Trainer> updatedTrainer = trainerDBService.selectByUsername(trainer.getUserName(), password);
        assertTrue(updatedTrainer.isPresent());
        System.out.println(trainer.isActive() + "   " + updatedTrainer.get().isActive());
        assertNotEquals(initialStatus, updatedTrainer.get().isActive());
    }

    @Test
    void changeStatus_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> trainerDBService.changeStatus(trainer, trainer.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainer with username: " + trainer.getUserName(), exception.getMessage());
    }

    @Test
    void update_ShouldUpdateTrainer_WhenAuthenticated() {
        String[] updates = {"New FirstName", "New LastName", "NewUsername", "NewPassword", "true", "FITNESS"};
        trainerDBService.update(trainer, trainer.getUserName(), password, updates);
        Optional<Trainer> updatedTrainer = trainerDBService.selectByUsername(trainer.getUserName(), password);

        assertTrue(updatedTrainer.isPresent());
        assertEquals("New FirstName", updatedTrainer.get().getFirstName());
        assertEquals("New LastName", updatedTrainer.get().getLastName());
        assertEquals(TrainingType.valueOf("FITNESS"), updatedTrainer.get().getSpecialization()); // Assuming valid TrainingType
    }

    @Test
    void update_ShouldThrowException_WhenAuthenticationFails() {
        String[] updates = {"New FirstName", "New LastName", "NEW_SPECIALIZATION"};
        SecurityException exception = assertThrows(SecurityException.class,
                () -> trainerDBService.update(trainer, trainer.getUserName(), "wrongPassword", updates));

        assertEquals("Authentication failed for trainer with username: " + trainer.getUserName(), exception.getMessage());
    }

    @Test
    void getTrainerTrainings_Test() {
        assertThrows(SecurityException.class, () -> trainerDBService.getTrainerTrainings(
                        trainer.getUserName(),
                        "wrongPassword",
                        LocalDate.now(),
                        LocalDate.now().plusDays(10),
                        "David.Jones",
                        TrainingType.ZUMBA),
                "Failed authentication should throw exception");


        List<Training> trainings = trainerDBService.getTrainerTrainings(
                trainer.getUserName(),
                password,
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                "Test.Trainee",
                trainer.getSpecialization());
        assertTrue(trainings.size() > 0);
        System.out.println("Trainings: " + trainings);
    }
}
