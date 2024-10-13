package com.gym_app.core.repo;

import com.gym_app.core.dto.Training;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TrainingRepository {
    private final HashMap<Long, Training> trainingRepository;

    public TrainingRepository(){
        trainingRepository = new HashMap<>();
    }

    public HashMap<Long, Training> getRepository(){
        return trainingRepository;
    }
}
