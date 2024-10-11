package com.gym_app.core.repo;

import com.gym_app.core.dto.Trainee;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TraineeRepository {
    private HashMap<String, Trainee> trainerRepository;

    public TraineeRepository(){
        trainerRepository = new HashMap<>();
    }

    public HashMap<String, Trainee> getRepository() {
        return trainerRepository;
    }
}