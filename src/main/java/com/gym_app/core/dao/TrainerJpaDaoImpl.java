package com.gym_app.core.dao;

import com.gym_app.core.dto.Trainer;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerJpaDaoImpl extends JpaDao<Trainer, Long> {

    public TrainerJpaDaoImpl(){
        super(Trainer.class);
    }

}
