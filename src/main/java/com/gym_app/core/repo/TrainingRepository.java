package com.gym_app.core.repo;

import com.gym_app.core.dto.Training;
import org.springframework.stereotype.Component;


@Component
public class TrainingRepository extends AbstractRepository<Long, Training> {

    public TrainingRepository() {
        super();
    }
}
