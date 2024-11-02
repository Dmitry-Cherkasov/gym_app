package com.gym_app.core.dao;

import com.gym_app.core.CoreApplication;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.util.PasswordGenerator;
import com.gym_app.core.util.TrainerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreApplication.class)
class TrainerJpaDaoImplTest {
    @Autowired
    private TrainerJpaDaoImpl trainerJpaDao;
    @Autowired
    private TrainerFactory trainerFactory;
    private Trainer[] trainers;
    private int dbInitialsize;

    @BeforeEach
    public void setup() {
        dbInitialsize = trainerJpaDao.getAll().size();
        trainers = new Trainer[5];

        trainers[0] = trainerFactory.createUser("John", "Brown", true, TrainingType.YOGA);
        trainers[1] = trainerFactory.createUser("Anna", "Smith", false, TrainingType.ZUMBA);
        trainers[2] = trainerFactory.createUser("Damian", "Wayne", true, TrainingType.RESISTANCE);
        trainers[3] = trainerFactory.createUser("Ronny", "Coleman", true, TrainingType.STRETCHING);
        trainers[4] = trainerFactory.createUser("Umfue", "Ousas", true, TrainingType.FITNESS);
        Arrays.stream(trainers).forEach(trainer -> {
            trainer.setPassword(PasswordGenerator.createPassword(6));
            trainer.setUserName(trainer.getFirstName()+"."+trainer.getLastName());
            trainer = trainerJpaDao.save(trainer);
        });
    }

    @AfterEach
    public void clear(){
        Arrays.stream(trainers).forEach(trainer -> trainerJpaDao.delete(trainer));
    }

    @Test
    public void saveTest(){
        Arrays.stream(trainers).forEach(trainer -> {
            assertFalse(trainer.getId() == null, "Saved trainer should have id");
        });
        Trainer nonSpecializedTrainer = trainerFactory.createUser("John", "Snow", true, TrainingType.ZUMBA);
        //no userName and password set
        assertThrows(RuntimeException.class, ()->trainerJpaDao.save(nonSpecializedTrainer), "Violating nullable constraint should throw an exception");

    }

    @Test
    public void getByIdTest(){
        Arrays.stream(trainers).forEach(trainer -> {
            Optional<Trainer> foundtrainer = trainerJpaDao.getById(trainer.getId());
            assertTrue(foundtrainer.isPresent(), "trainer should be present");
            assertEquals(trainer.getFirstName(), foundtrainer.get().getFirstName());
        });

        Optional<Trainer> nonExistenttrainer = trainerJpaDao.getById(999L);
        assertFalse(nonExistenttrainer.isPresent(), "trainer with non-existing id should not be present");
    }

    @Test
    public void getByUsernameTest(){
        Arrays.stream(trainers).forEach(trainer -> {
            Optional<Trainer> foundtrainer = trainerJpaDao.getByUserName(trainer.getUserName());
            assertTrue(foundtrainer.isPresent(), "trainer should be present");
            assertEquals(trainer.getFirstName(), foundtrainer.get().getFirstName());
        });

        Optional<Trainer> nonExistenttrainer = trainerJpaDao.getByUserName("non.existent");
        assertFalse(nonExistenttrainer.isPresent(), "trainer with non-existing username should not be present");
    }

    @Test
    public void deleteTest(){
        Trainer trainer = trainers[0];
        trainerJpaDao.delete(trainer);

        Optional<Trainer> foundtrainer = trainerJpaDao.getById(trainer.getId());
        assertFalse(foundtrainer.isPresent(), "Deleted trainer should not be present");
    }

    @Test
    public void deleteByUsernameTest(){
        Trainer trainer = trainers[0];
        trainerJpaDao.deleteByUserName(trainer.getUserName());

        Optional<Trainer> foundtrainer = trainerJpaDao.getByUserName(trainer.getUserName());
        assertFalse(foundtrainer.isPresent(), "Deleted trainer should not be present by username");
    }

    @Test
    public void getAllTest(){
        List<Trainer> alltrainers = trainerJpaDao.getAll();
        assertEquals(trainers.length + dbInitialsize, alltrainers.size(), "Should retrieve all saved trainers");
    }

    @Test
    public void updateTest(){

    }


}