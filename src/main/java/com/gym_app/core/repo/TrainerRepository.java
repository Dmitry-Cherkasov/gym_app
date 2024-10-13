package com.gym_app.core.repo;

import com.gym_app.core.dto.Trainer;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TrainerRepository {
    private final HashMap<String, Trainer> trainerRepository;

    public TrainerRepository(){
        trainerRepository = new HashMap<>();
    }

    public HashMap<String, Trainer> getRepository() {
        return trainerRepository;
    }
}
