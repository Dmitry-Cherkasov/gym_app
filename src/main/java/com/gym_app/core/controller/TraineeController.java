package com.gym_app.core.controller;

import com.gym_app.core.dto.auth.AuthenticationEntity;
import com.gym_app.core.dto.auth.TraineeRegistrationRequest;
import com.gym_app.core.dto.common.ToggleActiveRequest;
import com.gym_app.core.dto.common.Trainee;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.dto.profile.TraineeProfileResponse;
import com.gym_app.core.dto.profile.TraineeProfileUpdateRequest;
import com.gym_app.core.dto.profile.TrainerSummary;
import com.gym_app.core.dto.profile.TrainersListUpdateRequest;
import com.gym_app.core.dto.traininig.TrainingCreateRequest;
import com.gym_app.core.dto.traininig.TrainingInfo;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.services.TraineeDbService;
import com.gym_app.core.services.TrainerDBService;
import com.gym_app.core.util.TrainerSummaryMapper;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/trainee")
@Validated
@Api(value = "Trainee Management System", tags = "Trainees")
public class TraineeController {
    @Autowired
    AuthenticationEntity login;
    private final TraineeDbService traineeService;
    private final TrainerDBService trainerService;

    @Autowired
    public TraineeController(TraineeDbService traineeDbService, TrainerDBService trainerDBService) {
        traineeService = traineeDbService;
        trainerService = trainerDBService;
    }

    @PostMapping
    @ApiOperation(value = "Register a new Trainee", notes = "Creates a new trainee with the provided details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee successfully registered"),
            @ApiResponse(code = 400, message = "Bad Request - Invalid input")
    })
    public ResponseEntity<Map<String, String>> registerTrainee(
            @ApiParam(value = "Trainee request body", required = true)
            @Valid
            @RequestBody
            TraineeRegistrationRequest request) {

        Map<String, String> response = new HashMap<>();

        Trainee trainee = new Trainee(
                request.getFirstName(),
                request.getLastName(),
                "",
                "",
                true,
                request.getBirthDate(),
                request.getAddress()
        );
        try {
            trainee = traineeService.create(trainee);
        } catch (RuntimeException exception) {
            response.put("error", "Failed to register trainee: " + exception.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.put("username", trainee.getUserName());
        response.put("password", trainee.getPassword());

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{username}")
    @ApiOperation(value = "Get Trainee Profile", notes = "Fetches the profile of a specific trainee by username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched trainee profile"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    public ResponseEntity<TraineeProfileResponse> getTraineeProfile(
            @PathVariable
            @ApiParam(value = "Username of the trainee", required = true)
            @NotBlank(message = "Username is required")
            String username) {

        Optional<Trainee> traineeOptional = traineeService.selectByUsername(username, login.getPassword()); //needs password from session context
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            // Map trainee to response DTO
            TraineeProfileResponse response = mapTraineeToResponse(trainee);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    @ApiOperation(value = "Update Trainee Profile", notes = "Updates the profile of the logged-in trainee")
    public ResponseEntity<TraineeProfileResponse> updateTraineeProfile(
            @ApiParam(value = "Trainee profile update request body", required = true)
            @Valid @RequestBody
            TraineeProfileUpdateRequest request) {

        Optional<Trainee> traineeOpt = traineeService.selectByUsername(login.getUserName(), login.getPassword());
        if (traineeOpt.isPresent()) {
            Trainee oldTrainee = traineeOpt.get();
            traineeService.updateUser(
                    oldTrainee,
                    new String[]{
                            request.getFirstName(),
                            request.getLastName(),
                            request.getUserName(),
                            "", //Password can not be changed;
                            String.valueOf(request.getIsActive()),
                            request.getDateOfBirth().toString(),
                            request.getAddress()});
            Trainee updatedTrainee = traineeService.selectByUsername(oldTrainee.getUserName(), oldTrainee.getPassword()).get();
            TraineeProfileResponse response = mapTraineeToResponse(updatedTrainee);
            response.setUserName(updatedTrainee.getUserName());
            response.setUserName(updatedTrainee.getUserName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{username}")
    @ApiOperation(value = "Delete Trainee Profile", notes = "Deletes a trainee profile by username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee profile successfully deleted"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    public ResponseEntity<Void> deleteTraineeProfile(
            @ApiParam(value = "Username of the trainee to be deleted", required = true)
            @NotBlank(message = "Username is required") @PathVariable String username) {

        boolean isDeleted = traineeService.delete(username, login.getPassword());
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{username}/trainers")
    @ApiOperation(value = "Get Available Trainers", notes = "Fetches a list of trainers available for the specified trainee")
    public ResponseEntity<List<TrainerSummary>> getAvailableTrainers(
            @ApiParam(value = "Username of the trainee", required = true)
            @NotBlank(message = "Username is required") @PathVariable String username) {

        Optional<Trainee> traineeOpt = traineeService.selectByUsername(username, login.getPassword());
        if (traineeOpt.isPresent()) {
            List<TrainerSummary> trainers = traineeService.
                    getAvailableTrainers(login.getUserName(), login.getPassword()).
                    stream().
                    map(TrainerSummaryMapper::mapTrainerToSummary).
                    toList();

            return new ResponseEntity<>(trainers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/trainers")
    @ApiOperation(value = "Update Trainee's Trainers List", notes = "Updates the list of trainers assigned to a trainee")
    public ResponseEntity<List<TrainerSummary>> updateTraineeTrainerList(
            @ApiParam(value = "Trainee's trainers list update request", required = true)
            @Valid @RequestBody TrainersListUpdateRequest request) {

        Optional<Trainee> traineeOpt = traineeService.selectByUsername(request.getTraineeUsername(), login.getPassword());
        if (traineeOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Trainee trainee = traineeOpt.get();

        HashMap<String, Trainer> allTrainers = new HashMap<>();

        traineeService.getAvailableTrainers(login.getUserName(), login.getPassword()).
                forEach(trainer -> allTrainers.put(trainer.getUserName(), trainer));

        trainee.getTrainers().forEach(trainer -> allTrainers.put(trainer.getUserName(), trainer));
        List<Trainer> updatedTrainers = request.getTrainersList().stream().map(allTrainers::get).toList();

        updatedTrainers.forEach(trainer -> traineeService.addTrainerToList(login.getUserName(), login.getPassword(), trainer));

        List<TrainerSummary> response = updatedTrainers.
                stream()
                .map(TrainerSummaryMapper::mapTrainerToSummary)
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/trainings")
    @ApiOperation(value = "Get Trainee Trainings", notes = "Retrieves a list of trainings based on filter criteria")
    public ResponseEntity<List<TrainingInfo>> getTrainingsList(
            @ApiParam(value = "Trainee's username", required = true)
            @RequestParam @NotNull String username,
            @ApiParam(value = "Start date filter", required = false)
            @RequestParam(required = false) LocalDate periodFrom,
            @ApiParam(value = "End date filter", required = false)
            @RequestParam(required = false) LocalDate periodTo,
            @ApiParam(value = "Trainer's name filter", required = false)
            @RequestParam(required = false) String trainerName,
            @ApiParam(value = "Training type filter", required = false)
            @RequestParam(required = false) TrainingType trainingType) {
        Optional<Trainee> traineeOpt = traineeService.selectByUsername(username, login.getPassword());
        if (traineeOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Trainee trainee = traineeOpt.get();
        List<Training> trainings = traineeService.getTraineeTrainings(
                username,
                trainee.getPassword(),
                periodFrom,
                periodTo,
                trainerName,
                trainingType);

        List<TrainingInfo> response = trainings.stream()
                .map(training -> {
                    TrainingInfo trainingInfo = new TrainingInfo();
                    trainingInfo.setTrainingName(training.getTrainingName());
                    trainingInfo.setTrainingDate(training.getTrainingDate());
                    trainingInfo.setTrainingType(training.getTrainingType().toString());
                    trainingInfo.setTrainingDuration(training.getDuration());
                    trainingInfo.setTrainerName(training.getTrainer().getUserName());
                    return trainingInfo;
                })
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/trainings")
    public ResponseEntity<String> addTraining(@Valid @RequestBody TrainingCreateRequest request) {
        Optional<Trainee> traineeOpt = traineeService.selectByUsername(request.getTraineeUsername(), login.getPassword());
        Optional<Trainer> trainerOpt = traineeService.
                getAvailableTrainers(request.getTraineeUsername(), login.getPassword()).
                stream().
                filter(trainer -> trainer.getUserName().equals(request.getTrainerUsername())).findAny();

        if (traineeOpt.isEmpty() || trainerOpt.isEmpty()) {
            return new ResponseEntity<>("Trainer or trainee not found", HttpStatus.NOT_FOUND);
        }

        Trainer trainer = trainerOpt.get();
        Trainee trainee = traineeOpt.get();

        try {
            traineeService.addTraining(
                    trainee.getUserName(),
                    trainee.getPassword(),
                    trainer,
                    request.getTrainingName(),
                    trainer.getSpecialization(),
                    request.getTrainingDate(),
                    request.getTrainingDuration());

            return new ResponseEntity<>("Training successfully added", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(value = "/status")
    public ResponseEntity<String> toggleActiveStatus(@Valid @RequestBody ToggleActiveRequest request) {
        Optional<Trainee> traineeOpt = traineeService.selectByUsername(request.getUsername(), login.getPassword());

        if (traineeOpt.isEmpty()) {
            return new ResponseEntity<>("Trainee not found", HttpStatus.NOT_FOUND);
        }

        Trainee trainee = traineeOpt.get();

        try {
            if(trainee.isActive() != request.getIsActive()) {
                traineeService.changeStatus(trainee, trainee.getUserName(), trainee.getPassword());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update trainee status", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/training-types")
    public ResponseEntity<Map<String, TrainingType>> getTrainingTypes() {
        Map<String, TrainingType> response = new HashMap<>();
        Arrays.stream(TrainingType.values()).forEach(trainingType -> response.put(trainingType.toString(), trainingType));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private TraineeProfileResponse mapTraineeToResponse(Trainee trainee) {

        TraineeProfileResponse response = new TraineeProfileResponse();
        response.setFirstName(trainee.getFirstName());
        response.setLastName(trainee.getLastName());
        response.setDateOfBirth(trainee.getDateOfBirth());
        response.setAddress(trainee.getAddress());
        response.setIsActive(trainee.isActive());

        List<TrainerSummary> trainers = trainee.getTrainers().stream().map(trainer -> {
            TrainerSummary trainerInfo = new TrainerSummary();
            trainerInfo.setFirstName(trainer.getFirstName());
            trainerInfo.setLastName(trainer.getLastName());
            trainerInfo.setSpecialization(trainer.getSpecialization().toString());
            return trainerInfo;
        }).collect(Collectors.toList());

        response.setTrainers(trainers);
        return response;
    }
}
