package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainee;
import com.gym_app.core.repo.TraineeRepository;
import com.gym_app.core.util.PasswordGenerator;
import com.gym_app.core.util.UserNameGenerator;
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

    private HashMap<String, Trainee> getTraineeMap() {
        return this.traineeRepository.getRepository();  // Access the map through the repository
    }

    @Override
    public Optional<Trainee> getById(String id) {
        return Optional.ofNullable(getTraineeMap().get(id));
    }

    @Override
    public List<Trainee> getAll() {
        return new ArrayList<>(getTraineeMap().values());
    }

    @Override
    public Trainee save(Trainee trainee) {
        trainee.setPassword(PasswordGenerator.createPassword(10));
        trainee.setUserName(UserNameGenerator.generate(trainee.getFirstName(), trainee.getLastName(), getTraineeMap()));
        getTraineeMap().put(trainee.getUserName(), trainee);
        return trainee;
    }

    @Override
    public void delete(Trainee user) {
        getTraineeMap().values().removeIf(existingUser -> existingUser.getUserName().equals(user.getUserName()));
    }


    @Override
    public void update(Trainee trainee, String[] params) {
        Trainee existingTrainee = getTraineeMap().get(trainee.getUserName());
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

            getTraineeMap().put(trainee.getUserName(), existingTrainee);
        } else {
            throw new IllegalArgumentException("Trainee not found with user name: " + trainee.getUserName());
        }
    }
}
