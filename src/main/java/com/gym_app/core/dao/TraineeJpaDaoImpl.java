package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainee;
import org.springframework.stereotype.Repository;


@Repository
public class TraineeJpaDaoImpl extends JpaDao<Trainee, Long> {

    public TraineeJpaDaoImpl() {
        super(Trainee.class);
    }

}
