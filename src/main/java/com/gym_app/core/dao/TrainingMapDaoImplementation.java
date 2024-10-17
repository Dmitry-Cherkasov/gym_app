package com.gym_app.core.dao;

import com.gym_app.core.dto.Training;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.repo.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;


@Component
public class TrainingMapDaoImplementation implements Dao<Training, Long>{
    private final TrainingRepository trainingRepository;

    @Autowired
    public TrainingMapDaoImplementation(TrainingRepository trainingRepository){
        this.trainingRepository = trainingRepository;
    }
    

    @Override
    public Optional<Training> getById(Long id) {
        return Optional.ofNullable(trainingRepository .get(id));
    }

    @Override
    public List<Training> getAll() {
        return new ArrayList<>(trainingRepository .values());
    }

    @Override
    public Training save(Training training) {
        Random random = new Random();
        long id = random.nextLong(1000);
        while (trainingRepository .containsKey(id)) {
            id = random.nextLong(1000);
        }
        training.setTrainingId(id);
        trainingRepository.put(id, training);
        return training;
    }

    @Override
    public void delete(Training training) {
        trainingRepository.values().removeIf(existingTraining -> existingTraining.getTrainingId() == (training.getTrainingId()));
    }


    @Override
    public void update(Training training, String[] params) {
        Training existingTraining = trainingRepository .get(training.getTrainingId());
        if (params.length < 6) {
            throw new IllegalArgumentException("Invalid number of parameters. Expected 6 parameters.");
        }
        for(String element: params){
            if(element == null){
                return;
            }
        }
        if (existingTraining != null ) {
            existingTraining.setTraineeId(Long.parseLong(params[0]));
            existingTraining.setTrainerId(Long.parseLong(params[1]));
            existingTraining.setTrainingName(params[2]);
            existingTraining.setTrainingType(TrainingType.valueOf(params[3].toUpperCase()));
            existingTraining.setTrainingDate(LocalDate.parse(params[4]));
            existingTraining.setDuration(Integer.parseInt(params[5]));

            trainingRepository .put(training.getTrainingId(), existingTraining);
        } else {
            throw new IllegalArgumentException("Training not found with ID: " + training.getTrainingId());
        }
    }
}
