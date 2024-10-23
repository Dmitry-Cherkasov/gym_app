package com.gym_app.core.dao;

import com.gym_app.core.CoreApplication;
import com.gym_app.core.dto.Trainee;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.dto.Training;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.util.TraineeFactory;
import com.gym_app.core.util.TrainerFactory;
import com.gym_app.core.util.TrainingFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreApplication.class)
@AutoConfigureMockMvc
@Transactional
class TrainingJpaDaoTest {

    @Autowired
    private TrainingJpaDao trainingJpaDao;

    @Autowired
    private TraineeFactory traineeFactory;

    @Autowired
    private TrainerFactory trainerFactory;

    @Autowired
    private TrainingFactory trainingFactory;

    @Autowired
    TrainerJpaDaoImpl trainerJpaDao;
    @Autowired
    TraineeJpaDaoImpl traineeJpaDao;


    private Trainer trainer;
    private Trainee trainee;
    private Training training;
    private int initialDbSize;
    private boolean toBeLaunched;


    @BeforeEach
    public void setup() {
        initialDbSize = trainingJpaDao.getAll().size();
        trainer = trainerJpaDao.save(new Trainer("Red", "One", "Red.One", "password01", true, TrainingType.FITNESS));
        trainee = traineeJpaDao.save(new Trainee("Green", "One", "Green.One", "password02", true, LocalDate.parse("2020-01-05"), "Tokio"));
        training = trainingFactory.createTraining(trainee, trainer);
        training = trainingJpaDao.save(training);
        toBeLaunched = true;
    }

    @AfterEach
    public void cleanup() {
        if (training != null && toBeLaunched) {
            trainingJpaDao.delete(training);
        }
        if (trainer != null) {
            trainerJpaDao.delete(trainer);
        }
        if (trainee != null) {
            traineeJpaDao.delete(trainee);
        }
    }



    @Test
    public void saveTest() {
        assertFalse(trainingJpaDao.getAll().isEmpty());
        Training invalidTraining = new Training();
        assertThrows(RuntimeException.class, () -> trainingJpaDao.save(invalidTraining), "Saving an invalid training should throw an exception");
    }

    @Test
    public void getByIdTest() {
            Optional<Training> foundTraining = trainingJpaDao.getById(training.getTrainingId());
            assertTrue(foundTraining.isPresent(), "Training should be found by ID");
            assertEquals(training.getTrainingType(), foundTraining.get().getTrainingType());

        Optional<Training> nonExistentTraining = trainingJpaDao.getById(999L);
        assertFalse(nonExistentTraining.isPresent(), "Training with non-existing ID should not be present");
    }

    @Test
    public void getAllTest() {
        List<Training> allTrainings = trainingJpaDao.getAll();
        assertEquals(initialDbSize + 1, allTrainings.size(), "Should retrieve all saved trainings");
    }

    @Test
    public void deleteTest() {
        trainingJpaDao.delete(training);

        Optional<Training> foundTraining = trainingJpaDao.getById(training.getTrainingId());
        assertFalse(foundTraining.isPresent(), "Deleted training should not be found by ID");
        toBeLaunched = false;
//        training.setTrainingName("NewTrainingName");
//        training = trainingJpaDao.save(training);
    }

    @Test
    public void updateTest() {
        Training updatedTraining;
        try {
            updatedTraining = (Training) training.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        updatedTraining.setTrainingName("New Training name");
        trainingJpaDao.update(training,updatedTraining);
        assertEquals("New Training name", trainingJpaDao.getById(training.getTrainingId()).get().getTrainingName(),
                "Training name should be updated");

    }
}
