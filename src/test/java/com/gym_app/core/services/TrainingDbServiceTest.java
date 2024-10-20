package com.gym_app.core.services;

import com.gym_app.core.dao.TraineeJpaDaoImpl;
import com.gym_app.core.dao.TrainerJpaDaoImpl;
import com.gym_app.core.dao.TrainingJpaDao;
import com.gym_app.core.dto.Training;
import com.gym_app.core.dto.Trainee;
import com.gym_app.core.dto.Trainer;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.util.TrainingFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(MockitoExtension.class)
class TrainingDbServiceTest {
    @Autowired
    TrainerJpaDaoImpl trainerJpaDao;
    @Autowired
    TraineeJpaDaoImpl traineeJpaDao;
    @Autowired
    private TrainingDbService trainingDbService;
    @Autowired
    private TrainingFactory trainingFactory;
    @Autowired
    private TrainingJpaDao trainingJpaDao;

    private Training training;
    private Trainee trainee;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer("Red", "One", "Red.One", "password01", true, TrainingType.FITNESS);
        trainer = trainerJpaDao.save(trainer);
        trainee = new Trainee("Green", "One", "Green.One", "password02", true, LocalDate.parse("2020-01-05"), "Tokio");
        trainee = traineeJpaDao.save(trainee);

        training = trainingFactory.createTraining(trainee, trainer);
        training = trainingJpaDao.save(training);
    }

    @AfterEach
    void clear() {

    }

    @Test
    public void createTest() {
        trainingJpaDao.delete(training);
        assertFalse(trainingDbService.getDao().getById(training.getTrainingId()).isPresent(),
                "Deleted training should not be present in DB");
        training = trainingFactory.createTraining(trainee, trainer);
        training = trainingDbService.create(training);
        assertTrue(trainingDbService.getDao().getById(training.getTrainingId()).isPresent(),
                "Created training should be present in DB");
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
        List<Training> allTrainings = trainingDbService.getDao().getAll();
        assertTrue(allTrainings.size() > 0, "Should retrieve all saved trainings");
    }

    @Test
    public void deleteTest() {
        trainingDbService.delete(training.getTrainingId());

        Optional<Training> foundTraining = trainingJpaDao.getById(training.getTrainingId());
        assertFalse(foundTraining.isPresent(), "Deleted training should not be found by ID");
    }

    @Test
    public void updateTest() {
        String expectedString = "New Training name";
        String[] updates = new String[]{
                training.getTraineeId().toString(),
                training.getTrainerId().toString(),
                expectedString,
                training.getTrainingType().toString(),
                training.getTrainingDate().toString(),
                String.valueOf(training.getDuration())
                };

        trainingDbService.update(training, updates);
        assertEquals(expectedString, trainingDbService.getDao().getById(training.getTrainingId()).get().getTrainingName(),
                "Training name should be updated");
        Optional<Training> fetchedTraining = trainingJpaDao.getById(training.getTrainingId());
        assertTrue(fetchedTraining.isPresent(), "Updated training should be found");

    }
}
