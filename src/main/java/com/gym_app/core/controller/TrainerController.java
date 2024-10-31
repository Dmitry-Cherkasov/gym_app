package com.gym_app.core.controller;

import com.gym_app.core.dto.*;
import com.gym_app.core.services.TrainerDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/trainer")
public class TrainerController {
    @Autowired
    AuthenticationEntity login;
    private TrainerDBService trainerDBService;

    @Autowired
    public TrainerController(TrainerDBService trainerDBService) {
        this.trainerDBService = trainerDBService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> registerTrainer(@RequestBody RegistrationRequest request) {
        Map<String, String> response = new HashMap<>();
        Trainer trainer = new Trainer(
                request.getFirstName(),
                request.getLastName(),
                null,
                null,
                true,
                request.getTrainingType()
        );

        try {
            trainer = trainerDBService.create(trainer);
        } catch (RuntimeException exception) {
            response.put("error", "Failed to register trainer: " + exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.put("username", trainer.getUserName());
        response.put("password", trainer.getPassword());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<TrainerProfileResponse> getTraineeProfile(@PathVariable String username) {
        Optional<Trainer> trainerOptional = trainerDBService.selectByUsername(username, login.getPassword());
        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();
            TrainerProfileResponse response = mapTrainerToResponse(trainer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<TrainerProfileResponse> updateTrainerProfile(@RequestBody TrainerProfileUpdateRequest request) {
        Optional<Trainer> trainerOpt = trainerDBService.selectByUsername(login.getUserName(), login.getPassword());
        if (trainerOpt.isPresent()) {
            Trainer oldTrainer = trainerOpt.get();
            trainerDBService.updateUser(oldTrainer, new String[]{
                    request.getFirstName(),
                    request.getLastName(),
                    request.getUserName(),
                    "",
                    String.valueOf(request.getIsActive()),
                    request.getSpecialization().toString()
            });

            Trainer updatedTrainer = trainerDBService.selectByUsername(oldTrainer.getUserName(), oldTrainer.getPassword()).get();
            TrainerProfileResponse response = mapTrainerToResponse(updatedTrainer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/trainings")
    public ResponseEntity<List<TrainingInfo>> getTrainingsList(
            @RequestParam String username,
            @RequestParam(required = false) LocalDate periodFrom,
            @RequestParam(required = false) LocalDate periodTo,
            @RequestParam(required = false) String traineeName) {
        Optional<Trainer> trainerOpt = trainerDBService.selectByUsername(username, login.getPassword());
        if (trainerOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try{
        Trainer trainer = trainerOpt.get();
        List<Training> trainings = trainerDBService.getTrainerTrainings(
                username,
                login.getPassword(),
                periodFrom,
                periodTo,
                traineeName,
                trainer.getSpecialization());

        List<TrainingInfo> response = trainings.stream()
                .map(training -> {
                    TrainingInfo trainingInfo = new TrainingInfo();
                    trainingInfo.setTrainingName(training.getTrainingName());
                    trainingInfo.setTrainingDate(training.getTrainingDate());
                    trainingInfo.setTrainingType(training.getTrainingType().toString());
                    trainingInfo.setTrainingDuration(training.getDuration());
                    trainingInfo.setTraineeName(training.getTrainee().getUserName());
                    return trainingInfo;
                })
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(value = "/status")
    public ResponseEntity<String> toggleActiveStatus(@RequestBody ToggleActiveRequest request) {
        Optional<Trainer> trainerOpt = trainerDBService.selectByUsername(request.getUsername(), login.getPassword());

        if (trainerOpt.isEmpty()) {
            return new ResponseEntity<>("Trainer not found", HttpStatus.NOT_FOUND);
        }

        Trainer trainer = trainerOpt.get();

        try {
            if(trainer.isActive() != request.getIsActive()) {
                trainerDBService.changeStatus(trainer, trainer.getUserName(), trainer.getPassword());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update trainer status", HttpStatus.BAD_REQUEST);
        }
    }

    private TrainerProfileResponse mapTrainerToResponse(Trainer trainer) {

        TrainerProfileResponse response = new TrainerProfileResponse();
        response.setUserName(trainer.getUserName());
        response.setFirstName(trainer.getFirstName());
        response.setLastName(trainer.getLastName());
        response.setSpecialization(trainer.getSpecialization());
        response.setIsActive(trainer.isActive());

        List<TraineeSummary> trainees = trainer.getTrainees().stream().map(trainee -> {
            TraineeSummary traineeInfo = new TraineeSummary();
            traineeInfo.setUsername(trainee.getUserName());
            traineeInfo.setUsername(trainee.getUserName());
            traineeInfo.setFirstName(trainee.getFirstName());
            traineeInfo.setLastName(trainee.getLastName());
            return traineeInfo;
        }).collect(Collectors.toList());

        response.setTrainees(trainees);
        return response;
    }
}
