package com.gym_app.core.util;

import com.gym_app.core.dto.Trainer;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.CoreApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreApplication.class)
class TrainerFactoryTest {

    @Autowired
    private TrainerFactory trainerFactory;

    @Test
    void testCreateUserWithoutTrainingType() {
        Trainer trainer = trainerFactory.createUser("Jane", "Smith", true);

        assertNotNull(trainer);
        assertEquals("Jane", trainer.getFirstName());
        assertEquals("Smith", trainer.getLastName());
        assertTrue(trainer.isActive());
        assertNull(trainer.getSpecialization());
    }

    @Test
    void testCreateUserWithValidTrainingType() {
        Trainer trainer = trainerFactory.createUser("Jane", "Smith", true, "ZUMBA");

        assertNotNull(trainer);
        assertEquals("Jane", trainer.getFirstName());
        assertEquals("Smith", trainer.getLastName());
        assertTrue(trainer.isActive());
        assertEquals(TrainingType.ZUMBA, trainer.getSpecialization());
    }

    @Test
    void testCreateUserWithInvalidTrainingType() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerFactory.createUser("Jane", "Smith", true, "INVALID_TYPE");
        });

        assertEquals("Illegal training type argument: INVALID_TYPE", exception.getMessage());
    }
}
