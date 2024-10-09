package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainer;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.repo.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TrainerMapDaoImplementation implements Dao<Trainer, Long> {
    private final HashMap<Long, Trainer>  trainerRepository;

    @Autowired
    public TrainerMapDaoImplementation(TrainerRepository trainerRepository){
        this.trainerRepository = trainerRepository.getRepository();
    }

    @Override
    public Optional<Trainer> getById(Long id) {
        return Optional.ofNullable(trainerRepository.get(id));
    }

    @Override
    public List<Trainer> getAll() {
        return new ArrayList<>(trainerRepository.values());
    }

    @Override
    public Trainer save(Trainer trainer) {
        Random random = new Random();
        long id = random.nextLong(1000);
        while (trainerRepository.containsKey(id)) {
            id = random.nextLong(1000);
        }
        trainer.setUserId(id);
        trainerRepository.put(id, trainer);
        return trainer;
    }

    @Override
    public void delete(Trainer user) {
        trainerRepository.values().removeIf(existingUser -> existingUser.getUserId().equals(user.getUserId()));
    }

    @Override
    public void update(Trainer trainer, String[] params) {
        Trainer existingTrainer = trainerRepository.get(trainer.getUserId());
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
            existingTrainer.setSpecialization(TrainingType.valueOf(params[5]));

            trainerRepository.put(trainer.getUserId(), existingTrainer);
        } else {
        throw new IllegalArgumentException("Trainer not found with ID: " + trainer.getUserId());
    }
    }
}
