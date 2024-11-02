package com.gym_app.core.dao;

import com.gym_app.core.CoreApplication;
import com.gym_app.core.dto.common.Trainee;
import com.gym_app.core.util.PasswordGenerator;
import com.gym_app.core.util.TraineeFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = CoreApplication.class)
@AutoConfigureMockMvc
@Transactional
class TraineeJpaDaoImplTest {
    @Autowired
    private TraineeJpaDaoImpl traineeJpaDao;
    @Autowired
    private TraineeFactory traineeFactory;
    private Trainee[] trainees;
    private int dbInitialsize;
    private boolean toBeLaunched;

    @BeforeEach
    public void setup() {
        dbInitialsize = traineeJpaDao.getAll().size();
        trainees = new Trainee[5];

        trainees[0] = traineeFactory.createUser("John", "Wick", true, LocalDate.now().minusYears(20), "Hotel Continental");
        trainees[1] = traineeFactory.createUser("Alice", "Smith", false, LocalDate.now().minusYears(21), null);
        trainees[2] = traineeFactory.createUser("Bruce", "Wayne", true, LocalDate.now().minusYears(22), "Bat-cave");
        trainees[3] = traineeFactory.createUser("Diana", "Prince", true, LocalDate.now().minusYears(23), "Greece");
        trainees[4] = traineeFactory.createUser("Clark", "Kent", true, LocalDate.now().minusYears(24), "Krypton");
        Arrays.stream(trainees).forEach(trainee -> {
            trainee.setPassword(PasswordGenerator.createPassword(6));
            trainee.setUserName(trainee.getFirstName()+"."+trainee.getLastName());
            trainee = traineeJpaDao.save(trainee);
        });
        toBeLaunched = true;
    }

    @AfterEach
    public void clear(){
        if(toBeLaunched)
        Arrays.stream(trainees).forEach(trainee -> traineeJpaDao.delete(trainee));
    }

    @Test
    public void saveTest(){
        Arrays.stream(trainees).forEach(trainee -> {
            assertFalse(trainee.getId() == null, "Saved trainee should have id");
          });

    }

    @Test
    public void getByIdTest(){
        Arrays.stream(trainees).forEach(trainee -> {
            Optional<Trainee> foundTrainee = traineeJpaDao.getById(trainee.getId());
            assertTrue(foundTrainee.isPresent(), "Trainee should be present");
            assertEquals(trainee.getFirstName(), foundTrainee.get().getFirstName());
        });

        Optional<Trainee> nonExistentTrainee = traineeJpaDao.getById(999L);
        assertFalse(nonExistentTrainee.isPresent(), "Trainee with non-existing id should not be present");
    }

    @Test
    public void getByUsernameTest(){
        Arrays.stream(trainees).forEach(trainee -> {
            Optional<Trainee> foundTrainee = traineeJpaDao.getByUserName(trainee.getUserName());
            assertTrue(foundTrainee.isPresent(), "Trainee should be present");
            assertEquals(trainee.getFirstName(), foundTrainee.get().getFirstName());
        });

        Optional<Trainee> nonExistentTrainee = traineeJpaDao.getByUserName("non.existent");
        assertFalse(nonExistentTrainee.isPresent(), "Trainee with non-existing username should not be present");
    }

    @Test
    public void deleteTest(){
        Trainee trainee = trainees[0];
        traineeJpaDao.delete(trainee);

        Optional<Trainee> foundTrainee = traineeJpaDao.getById(trainee.getId());
        assertFalse(foundTrainee.isPresent(), "Deleted trainee should not be present");
        toBeLaunched = false;
    }

    @Test
    public void deleteByUsernameTest(){
        Trainee trainee = trainees[0];
        traineeJpaDao.deleteByUserName(trainee.getUserName());

        Optional<Trainee> foundTrainee = traineeJpaDao.getByUserName(trainee.getUserName());
        assertFalse(foundTrainee.isPresent(), "Deleted trainee should not be present by username");
    }

    @Test
    public void getAllTest(){
        System.out.println(this.getClass().getSimpleName());
        List<Trainee> allTrainees = traineeJpaDao.getAll();
        assertEquals(trainees.length + dbInitialsize, allTrainees.size(), "Should retrieve all saved trainees");
    }

    @Test
    public void updateTest(){

    }


}