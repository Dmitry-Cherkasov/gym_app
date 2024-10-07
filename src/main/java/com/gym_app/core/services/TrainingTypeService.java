package com.gym_app.core.services;

import com.gym_app.core.dao.TrainingTypeDao;
import com.gym_app.core.enums.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingTypeService {
    TrainingTypeDao  trainingTypeDao;

    @Autowired
    public TrainingTypeService(TrainingTypeDao trainingTypeDao){
        this.trainingTypeDao = trainingTypeDao;
    }

    public TrainingType select(long id){
        Optional<TrainingType> trainingType = trainingTypeDao.getById(id);
        return trainingType.orElseThrow(() -> new RuntimeException("TrainingType not found with id: " + id));
    }
}
