package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainee;
import com.gym_app.core.repo.TraineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;


@Component
public class TraineeMapDaoImplementation implements Dao<Trainee, Long>{
    private final HashMap<Long, Trainee> traineeRepository;

    @Autowired
    public TraineeMapDaoImplementation(TraineeRepository traineeRepository){
        this.traineeRepository = traineeRepository.getRepository();
    }

    @Override
    public Optional<Trainee> getById(Long id) {
        return Optional.ofNullable(traineeRepository.get(id));
    }

    @Override
    public List<Trainee> getAll() {
        return new ArrayList<>(traineeRepository.values());
    }

    @Override
    public Trainee save(Trainee trainer) {
        Random random = new Random();
        long id = random.nextLong(1000);
        while (traineeRepository.containsKey(id)) {
            id = random.nextLong(1000);
        }
        trainer.setUserId(id);
        traineeRepository.put(id, trainer);
        return trainer;
    }

    @Override
    public void delete(Trainee user) {
        traineeRepository.values().removeIf(existingUser -> existingUser.getUserId().equals(user.getUserId()));
    }


    @Override
    public void update(Trainee trainee, String[] params) {
        Trainee existingTrainee = traineeRepository.get(trainee.getUserId());
        if (params.length < 7) {
            throw new IllegalArgumentException("Invalid number of parameters. Expected 7 parameters.");
        }
        for(String element: params){
            if(element == null){
                return;
            }
        }
        if (existingTrainee != null ) {
            existingTrainee.setFirstName(params[0]);
            existingTrainee.setLastName(params[1]);
            existingTrainee.setUserName(params[2]);
            existingTrainee.setPassword(params[3]);
            existingTrainee.setActive(Boolean.parseBoolean(params[4]));
            LocalDate changedDate = LocalDate.parse(params[5]);
            existingTrainee.setDateOfBirth(changedDate);
            existingTrainee.setAddress(params[6]);

            traineeRepository.put(trainee.getUserId(), existingTrainee);
        } else {
            throw new IllegalArgumentException("Trainee not found with ID: " + trainee.getUserId());
        }
    }
}
