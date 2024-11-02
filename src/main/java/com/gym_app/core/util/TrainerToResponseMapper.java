package com.gym_app.core.util;

import com.gym_app.core.dto.profile.TraineeSummary;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.dto.profile.TrainerProfileResponse;

import java.util.List;
import java.util.stream.Collectors;

public class TrainerToResponseMapper {

    public static TrainerProfileResponse mapTrainerToResponse(Trainer trainer) {

        TrainerProfileResponse response = new TrainerProfileResponse();
        response.setUserName(trainer.getUserName());
        response.setFirstName(trainer.getFirstName());
        response.setLastName(trainer.getLastName());
        response.setSpecialization(trainer.getSpecialization());
        response.setIsActive(trainer.isActive());
        List<TraineeSummary> trainees = trainer.getTrainees().stream().map(trainee -> {
            TraineeSummary traineeInfo = new TraineeSummary();
            traineeInfo.setUsername(trainer.getUserName());
            traineeInfo.setUsername(trainer.getUserName());
            traineeInfo.setFirstName(trainer.getFirstName());
            traineeInfo.setLastName(trainer.getLastName());
            return traineeInfo;
        }).collect(Collectors.toList());

        response.setTrainees(trainees);
        return response;
    }
}
