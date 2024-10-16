package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainee;
import com.gym_app.core.repo.TraineeRepository;
import com.gym_app.core.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;


@Component
public class TraineeMapDaoImplementation implements Dao<Trainee, String>{
    private final TraineeRepository traineeRepository;

    @Autowired
    public TraineeMapDaoImplementation(TraineeRepository traineeRepository){
        this.traineeRepository = traineeRepository;
    }
    

    @Override
    public Optional<Trainee> getById(String id) {
        return Optional.ofNullable(traineeRepository.get(id));
    }

    @Override
    public List<Trainee> getAll() {
        return new ArrayList<>(traineeRepository.values());
    }

    @Override
    public Trainee save(Trainee trainee) {
        trainee.setPassword(PasswordGenerator.createPassword(10));
        trainee.setUserName(generate(trainee.getFirstName(), trainee.getLastName()));
        traineeRepository.put(trainee.getUserName(), trainee);
        return trainee;
    }

    @Override
    public void delete(Trainee user) {
        traineeRepository.values().removeIf(existingUser -> existingUser.getUserName().equals(user.getUserName()));
    }


    @Override
    public void update(Trainee trainee, String[] params) {
        Trainee existingTrainee = traineeRepository.get(trainee.getUserName());
        if (params.length < 7) {
            throw new IllegalArgumentException("Invalid number of parameters. Expected 7 parameters.");
        }
        for(String element: params){
            if(element == null){
                throw new IllegalArgumentException("Invalid parameter with Null value.");
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

            traineeRepository.put(trainee.getUserName(), existingTrainee);
        } else {
            throw new IllegalArgumentException("Trainee not found with user name: " + trainee.getUserName());
        }
    }

    private String generate(String firstName, String lastName){
        String baseUserName = firstName + "." + lastName;
        String userName = baseUserName;
        int serialNumber = 1;

        while (traineeRepository.containsKey(userName)) {
            userName = baseUserName + serialNumber;
            serialNumber++;
        }
        return userName;
    }
}
