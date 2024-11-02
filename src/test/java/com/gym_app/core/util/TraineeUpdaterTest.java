package com.gym_app.core.util;

import com.gym_app.core.dto.common.Trainee;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TraineeUpdaterTest {

    @Test
    public void updateTrainee_ValidParams_ShouldUpdateTrainee() {
        // Arrange
        Trainee trainee = new Trainee();
        String[] params = {"John", "Doe", "john.doe", "securePassword123", "true", "2000-01-01", "123 Street"};

        // Act
        Trainee updatedTrainee = TraineeUpdater.updateTrainee(trainee, params);

        // Assert
        assertEquals("John", updatedTrainee.getFirstName());
        assertEquals("Doe", updatedTrainee.getLastName());
        assertTrue(updatedTrainee.isActive());
        assertEquals(LocalDate.of(2000, 1, 1), updatedTrainee.getDateOfBirth());
        assertEquals("123 Street", updatedTrainee.getAddress());
    }

    @Test
    public void updateTrainee_NullTrainee_ShouldThrowException() {
        // Arrange
        String[] params = {"John", "Doe", "john.doe", "securePassword123", "true", "2000-01-01", "123 Street"};

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> TraineeUpdater.updateTrainee(null, params));

        assertEquals("Trainee object cannot be null.", exception.getMessage());
    }

    @Test
    public void updateTrainee_InsufficientParams_ShouldThrowException() {
        // Arrange
        Trainee trainee = new Trainee();
        String[] params = {"John", "Doe", "john.doe"};  // Only 3 parameters instead of 7

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> TraineeUpdater.updateTrainee(trainee, params));

        assertEquals("Invalid number of parameters. Expected 7 parameters.", exception.getMessage());
    }

    @Test
    public void updateTrainee_NullParamValue_ShouldThrowException() {
        // Arrange
        Trainee trainee = new Trainee();
        String[] params = {"John", "Doe", null, "securePassword123", "true", "2000-01-01", "123 Street"};

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> TraineeUpdater.updateTrainee(trainee, params));

        assertEquals("Invalid parameter with Null value.", exception.getMessage());
    }

    @Test
    public void updateTrainee_InvalidDate_ShouldThrowException() {
        // Arrange
        Trainee trainee = new Trainee();
        String[] params = {"John", "Doe", "john.doe", "securePassword123", "true", "invalid-date", "123 Street"};

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> TraineeUpdater.updateTrainee(trainee, params), "Should throw exception with wrong format date value");

    }
}
