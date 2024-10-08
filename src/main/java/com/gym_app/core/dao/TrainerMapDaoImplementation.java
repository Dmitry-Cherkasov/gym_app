package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainer;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.repo.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrainerMapDaoImplementation implements TrainerDao {
    private final HashMap<Long, Trainer> trainerRepository;

    @Autowired
    public TrainerMapDaoImplementation(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository.getTrainerRepository();
    }

    @Override
    public Optional<Trainer> getById(Long aLong) {
        return Optional.ofNullable(trainerRepository.get(aLong));
    }

    @Override
    public List<Trainer> getAll() {
        return trainerRepository.values().stream().toList();
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
    public void delete(Trainer trainer) {
        Optional<Long> idOptional = trainerRepository
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getUserId().equals(trainer.getUserId()))
                .map(Map.Entry::getKey)
                .findAny();

        idOptional.ifPresent(trainerRepository::remove);
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

            // Put the updated trainer back into the map
            trainerRepository.put(trainer.getUserId(), existingTrainer);
        } else {
        throw new IllegalArgumentException("Trainer not found with ID: " + trainer.getUserId());
    }
    }
}
