package com.gym_app.core.util;

import com.gym_app.core.CoreApplication;
import com.gym_app.core.dto.Trainee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = CoreApplication.class)
class TraineeFactoryTest {

    @Autowired
    private TraineeFactory traineeFactory;

    @Test
    void testCreateUserWithoutExtraArgs() {
        Trainee trainee = traineeFactory.createUser("John", "Doe", true);

        assertNotNull(trainee);
        assertEquals("John", trainee.getFirstName());
        assertEquals("Doe", trainee.getLastName());
        assertNull(trainee.getDateOfBirth());
        assertNull(trainee.getAddress());
        assertTrue(trainee.isActive());

    }

    @Test
    void testCreateUserWithDateOfBirth() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        Trainee trainee = traineeFactory.createUser("John", "Doe", true, birthDate);

        assertNotNull(trainee);
        assertEquals("John", trainee.getFirstName());
        assertEquals("Doe", trainee.getLastName());
        assertEquals(birthDate, trainee.getDateOfBirth());
        assertNull(trainee.getAddress());
        assertTrue(trainee.isActive());
    }

    @Test
    void testCreateUserWithDateOfBirthAndAddress() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        Trainee trainee = traineeFactory.createUser("John", "Doe", true, birthDate, address);

        assertNotNull(trainee);
        assertEquals("John", trainee.getFirstName());
        assertEquals("Doe", trainee.getLastName());
        assertEquals(birthDate, trainee.getDateOfBirth());
        assertEquals(address, trainee.getAddress());
        assertTrue(trainee.isActive());
    }
  
}