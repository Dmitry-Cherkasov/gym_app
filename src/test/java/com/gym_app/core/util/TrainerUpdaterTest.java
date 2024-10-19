package com.gym_app.core.util;

import com.gym_app.core.dto.Trainer;
import com.gym_app.core.enums.TrainingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainerUpdaterTest {

    @Test
    public void updateTrainer_ValidParams_ShouldUpdateTrainer() {
        // Arrange
        Trainer trainer = new Trainer();
        String[] params = {"Jane", "Doe", "jane.doe", "securePassword123", "true", "FITNESS"};

        // Act
        Trainer updatedTrainer = TrainerUpdater.updateTrainer(trainer, params);

        // Assert
        assertEquals("Jane", updatedTrainer.getFirstName());
        assertEquals("Doe", updatedTrainer.getLastName());
        assertTrue(updatedTrainer.isActive());
        assertEquals(TrainingType.FITNESS, updatedTrainer.getSpecialization());
    }

    @Test
    public void updateTrainer_NullTrainer_ShouldThrowException() {
        // Arrange
        String[] params = {"Jane", "Doe", "jane.doe", "securePassword123", "true", "CARDIO"};

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> TrainerUpdater.updateTrainer(null, params));

        assertEquals("trainer object cannot be null.", exception.getMessage());
    }

    @Test
    public void updateTrainer_InsufficientParams_ShouldThrowException() {
        // Arrange
        Trainer trainer = new Trainer();
        String[] params = {"Jane", "Doe", "jane.doe"}; // Only 3 parameters instead of 7

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> TrainerUpdater.updateTrainer(trainer, params));

        assertEquals("Invalid number of parameters. Expected 7 parameters.", exception.getMessage());
    }

    @Test
    public void updateTrainer_NullParamValue_ShouldThrowException() {
        // Arrange
        Trainer trainer = new Trainer();
        String[] params = {"Jane", "Doe", null, "securePassword123", "true", "CARDIO"};

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> TrainerUpdater.updateTrainer(trainer, params));

        assertEquals("Invalid parameter with Null value.", exception.getMessage());
    }

    @Test
    public void updateTrainer_InvalidTrainingType_ShouldThrowException() {
        // Arrange
        Trainer trainer = new Trainer();
        String[] params = {"Jane", "Doe", "jane.doe", "securePassword123", "true", "INVALID_TYPE"};

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> TrainerUpdater.updateTrainer(trainer, params));

        assertEquals("Invalid training type value: INVALID_TYPE", exception.getMessage());
    }
}
