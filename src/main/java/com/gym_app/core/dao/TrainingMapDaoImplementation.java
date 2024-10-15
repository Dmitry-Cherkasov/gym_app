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

    private HashMap<Long, Training> getTrainingMap() {
        return this.trainingRepository.getRepository();
    }

    @Override
    public Optional<Training> getById(Long id) {
        return Optional.ofNullable(getTrainingMap() .get(id));
    }

    @Override
    public List<Training> getAll() {
        return new ArrayList<>(getTrainingMap() .values());
    }

    @Override
    public Training save(Training training) {
        Random random = new Random();
        long id = random.nextLong(1000);
        while (getTrainingMap() .containsKey(id)) {
            id = random.nextLong(1000);
        }
        training.setServiceId(id);
        getTrainingMap() .put(id, training);
        return training;
    }

    @Override
    public void delete(Training training) {
        getTrainingMap().values().removeIf(existingTraining -> existingTraining.getServiceId() == (training.getServiceId()));
    }


    @Override
    public void update(Training training, String[] params) {
        Training existingTraining = getTrainingMap() .get(training.getServiceId());
        if (params.length < 6) {
            throw new IllegalArgumentException("Invalid number of parameters. Expected 6 parameters.");
        }
        for(String element: params){
            if(element == null){
                return;
            }
        }
        if (existingTraining != null ) {
            existingTraining.setConsumer(params[0]);
            existingTraining.setSupplierId(params[1]);
            existingTraining.setServiceName(params[2]);
            existingTraining.setTrainingType(TrainingType.valueOf(params[3].toUpperCase()));
            existingTraining.setServiceDate(LocalDate.parse(params[4]));
            existingTraining.setDuration(Integer.parseInt(params[5]));

            getTrainingMap() .put(training.getServiceId(), existingTraining);
        } else {
            throw new IllegalArgumentException("Training not found with ID: " + training.getServiceId());
        }
    }
}
