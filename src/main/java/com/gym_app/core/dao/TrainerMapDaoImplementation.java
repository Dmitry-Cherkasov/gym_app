package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainer;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.repo.TrainerRepository;
import com.gym_app.core.util.PasswordGenerator;
import com.gym_app.core.util.UserNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TrainerMapDaoImplementation implements Dao<Trainer, String> {
    private final TrainerRepository  trainerRepository;

    @Autowired
    public TrainerMapDaoImplementation(TrainerRepository trainerRepository){
        this.trainerRepository = trainerRepository;
    }

    private HashMap<String, Trainer> getTrainerMap() {
        return this.trainerRepository.getRepository();  // Access the map through the repository
    }

    @Override
    public Optional<Trainer> getById(String userName) {
        return Optional.ofNullable(getTrainerMap().get(userName));
    }

    @Override
    public List<Trainer> getAll() {
        return new ArrayList<>(getTrainerMap().values());
    }

    @Override
    public Trainer save(Trainer trainer) {
        trainer.setPassword(PasswordGenerator.createPassword(10));
        trainer.setUserName(UserNameGenerator.generate(trainer.getFirstName(), trainer.getLastName(), getTrainerMap()));
        getTrainerMap().put(trainer.getUserName(), trainer);
        return trainer;
    }

    @Override
    public void delete(Trainer user) {
        getTrainerMap().values().removeIf(existingUser -> existingUser.getUserName().equals(user.getUserName()));
    }

    @Override
    public void update(Trainer trainer, String[] params) {
        Trainer existingTrainer = getTrainerMap().get(trainer.getUserName());
        if (params.length < 6) {
            throw new IllegalArgumentException("Invalid number of parameters. Expected 6 parameters.");
        }
        for(String element: params){
            if(element == null){
                return;
            }
        }
        if (existingTrainer != null ) {
            existingTrainer.setFirstName(params[0]);
            existingTrainer.setLastName(params[1]);
            existingTrainer.setUserName(params[2]);
            existingTrainer.setPassword(params[3]);
            existingTrainer.setActive(Boolean.parseBoolean(params[4]));
            existingTrainer.setSpecialization(TrainingType.valueOf(params[5].toUpperCase()));

            getTrainerMap().put(trainer.getUserName(), existingTrainer);
        } else {
        throw new IllegalArgumentException("Trainer not found with UserName: " + trainer.getUserName());
    }
    }
}
