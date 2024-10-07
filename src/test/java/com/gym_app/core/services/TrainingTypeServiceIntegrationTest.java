package com.gym_app.core.services;

import com.gym_app.core.CoreApplication;
import com.gym_app.core.enums.TrainingType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreApplication.class)
public class TrainingTypeServiceIntegrationTest {

    @Autowired
    private TrainingTypeService trainingTypeService;

    @Test
    public void testSelectTrainingType() {
        TrainingType trainingType = trainingTypeService.select(1L);
        assertNotNull(trainingType);
        assertEquals("FITNESS", trainingType.toString());
        System.out.println("Retrieved Training Type: " + trainingType);

        assertThrows(RuntimeException.class, () -> trainingTypeService.select(8L),
                "Expected to throw RuntimeException for non-existing ID");

    }
}