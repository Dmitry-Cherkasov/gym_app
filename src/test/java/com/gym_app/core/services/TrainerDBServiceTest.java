package com.gym_app.core.services;
import com.gym_app.core.dao.TrainerJpaDaoImpl;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.util.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TrainerDBServiceTest {

    @Autowired
    private TrainerDBService trainerDBService;

    @Autowired
    private TrainerJpaDaoImpl trainerJpaDao;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        // Create a new Trainer instance
        trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUserName("john.doe");
        trainer.setPassword(PasswordGenerator.createPassword(10)); // Replace with your hashing logic
        trainer.setActive(true);
        trainer.setSpecialization(TrainingType.ZUMBA); // Assuming GROUP is a valid specialization
        trainer = trainerJpaDao.save(trainer); // Save the trainer to the database
    }

    @Test
    void create_ShouldSaveTrainer_WhenSuccessful() {
        Trainer newTrainer = new Trainer();
        newTrainer.setFirstName("Jane");
        newTrainer.setLastName("Doe");
        newTrainer.setActive(true);
        newTrainer.setSpecialization(TrainingType.FITNESS); // Assuming PERSONAL is a valid specialization

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
        assertDoesNotThrow(() -> trainerDBService.delete(trainer.getId(), trainer.getUserName(), trainer.getPassword()));

        Optional<Trainer> deletedTrainer = trainerJpaDao.getById(trainer.getId());
        assertFalse(deletedTrainer.isPresent());
    }

    @Test
    void delete_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> trainerDBService.delete(trainer.getId(), trainer.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainer with username: john.doe", exception.getMessage());
    }

    @Test
    void selectByUsername_ShouldReturnTrainer_WhenAuthenticated() {
        Optional<Trainer> foundTrainer = trainerDBService.selectByUsername("john.doe", trainer.getPassword());

        assertTrue(foundTrainer.isPresent());
        assertEquals(trainer.getId(), foundTrainer.get().getId());
    }

    @Test
    void selectByUsername_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> trainerDBService.selectByUsername("john.doe", "wrongPassword"));

        assertEquals("Authentication failed for trainer with username: john.doe", exception.getMessage());
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenAuthenticated() {
        String newPassword = "newPassword123";
        trainerDBService.changePassword(newPassword, trainer.getUserName(), trainer.getPassword());

        Optional<Trainer> updatedTrainer = trainerJpaDao.getByUserName(trainer.getUserName());
        assertTrue(updatedTrainer.isPresent());
        assertEquals(newPassword, updatedTrainer.get().getPassword()); // Ensure to verify the password hash if using hashing
    }

    @Test
    void changePassword_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> trainerDBService.changePassword("newPassword123", trainer.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainer with username: john.doe", exception.getMessage());
    }

    @Test
    void changeStatus_ShouldToggleTrainerStatus_WhenAuthenticated() {
        boolean initialStatus = trainer.isActive();
        trainerDBService.changeStatus(trainer, trainer.getUserName(), trainer.getPassword());
        Optional<Trainer> updatedTrainer = trainerJpaDao.getByUserName(trainer.getUserName());
        assertTrue(updatedTrainer.isPresent());
        System.out.println(trainer.isActive() + "   " + updatedTrainer.get().isActive());
        assertNotEquals(initialStatus, updatedTrainer.get().isActive());
    }

    @Test
    void changeStatus_ShouldThrowException_WhenAuthenticationFails() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> trainerDBService.changeStatus(trainer, trainer.getUserName(), "wrongPassword"));

        assertEquals("Authentication failed for trainer with username: john.doe", exception.getMessage());
    }

    @Test
    void update_ShouldUpdateTrainer_WhenAuthenticated() {
        String[] updates = {"New FirstName", "New LastName", "NewUsername", "NewPassword", "true", "FITNESS"};
        trainerDBService.update(trainer, trainer.getUserName(), trainer.getPassword(), updates);

        Optional<Trainer> updatedTrainer = trainerJpaDao.getByUserName(trainer.getUserName());
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

        assertEquals("Authentication failed for trainer with username: john.doe", exception.getMessage());
    }
}
