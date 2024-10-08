package com.gym_app.core.services;

import com.gym_app.core.CoreApplication;
import com.gym_app.core.dto.Trainee;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.util.CreatePolicy;
import com.gym_app.core.util.TraineeFactory;
import com.gym_app.core.util.TrainerFactory;
import com.gym_app.core.util.UserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

@SpringBootTest(classes = CoreApplication.class)
public class FactoryTest {
    @Autowired
    UserFactory userFactory;
    @Autowired
    TrainerFactory trainerFactory;
    @Autowired
    TraineeFactory traineeFactory;

    @Test
    public void testFactories(){
        Trainer trainer = trainerFactory.createTrainer("Susan", "Musan", true, TrainingType.FITNESS);
        System.out.println(trainer);
        assertEquals("Susan.Musan", trainer.getUserName());
        Trainee trainee = traineeFactory.createTrainee("Bob", "Rob", false, LocalDate.now(), "Somwhere");
        System.out.println(trainee);
        assertEquals("Bob.Rob", trainee.getUserName());
    }

}
