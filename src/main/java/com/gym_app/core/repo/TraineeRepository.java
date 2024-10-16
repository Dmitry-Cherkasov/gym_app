package com.gym_app.core.repo;

import com.gym_app.core.dto.Trainee;
import org.springframework.stereotype.Component;


@Component
public class TraineeRepository extends AbstractRepository<String, Trainee> {

    public TraineeRepository(){
        super();
    }
}
