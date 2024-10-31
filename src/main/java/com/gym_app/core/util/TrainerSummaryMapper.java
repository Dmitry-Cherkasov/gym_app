package com.gym_app.core.util;

import com.gym_app.core.dto.Trainer;
import com.gym_app.core.dto.TrainerSummary;

public class TrainerSummaryMapper {

    public static TrainerSummary mapTrainerToSummary(Trainer trainer){
        TrainerSummary summary = new TrainerSummary();
        summary.setUsername(trainer.getUserName());
        summary.setFirstName(trainer.getFirstName());
        summary.setLastName(trainer.getLastName());
        summary.setSpecialization(trainer.getSpecialization().toString());
        return summary;
    }
}
