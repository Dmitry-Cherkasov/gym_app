package com.gym_app.core.repo;

import com.gym_app.core.dto.common.Trainer;
import org.springframework.stereotype.Component;


@Component
public class TrainerRepository extends  AbstractRepository <String, Trainer> {

    public TrainerRepository(){
        super();
    }
}
