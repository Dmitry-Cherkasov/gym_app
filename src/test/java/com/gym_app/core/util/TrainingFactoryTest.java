package com.gym_app.core.util;

import com.gym_app.core.CoreApplication;
import com.gym_app.core.dto.ServiceType;
import com.gym_app.core.dto.Training;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreApplication.class)
class TrainingFactoryTest {
    @Autowired
    TrainingFactory trainingFactory;

    @Test
    public void testCreateTraining(){
        Training training = trainingFactory.createService(1L, 2L, "Full Body Fitness", LocalDate.now().plusDays(6), 90, "FITNESS");
        assertInstanceOf(Training.class, training);

        assertThrows(IllegalArgumentException.class, ()->trainingFactory.createService(1L, 2L, "Full Body Fitness", LocalDate.now().plusDays(6), 90, training),
                "Passing invalid training type argument should throw an exception");

    }


}