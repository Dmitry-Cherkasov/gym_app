package com.gym_app.core.controller;

import com.gym_app.core.dto.auth.AuthenticationEntity;
import com.gym_app.core.dto.auth.TrainerRegistrationRequest;
import com.gym_app.core.dto.common.ToggleActiveRequest;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.dto.profile.TraineeSummary;
import com.gym_app.core.dto.profile.TrainerProfileResponse;
import com.gym_app.core.dto.profile.TrainerProfileUpdateRequest;
import com.gym_app.core.dto.traininig.TrainingInfo;
import com.gym_app.core.services.JwtTokenProvider;
import com.gym_app.core.services.TrainerDBService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/trainer")
@Validated
@Tag(description = "Trainer Management System", name = "Trainers")
public class TrainerController {
    @Autowired
    AuthenticationEntity login;
    private final TrainerDBService trainerDBService;
    @Autowired
    public JwtTokenProvider jwtTokenProvider;

    @Autowired
    public TrainerController(TrainerDBService trainerDBService) {
        this.trainerDBService = trainerDBService;
    }

    @Operation( summary= "Register a new Trainer", description = "Creates a new trainer with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer successfully registered"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input")
    })
    @PostMapping
    public ResponseEntity<Map<String, String>> registerTrainer(
            @Parameter(description = "New trainer request body", required = true)
            @Valid @RequestBody
            TrainerRegistrationRequest request) {
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

        String token = jwtTokenProvider.generateToken(trainer.getUserName());
        response.put("token", token);
        response.put("userName", trainer.getUserName());
        response.put("password", trainer.getPassword());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get Trainer Profile", description = "Fetches the profile of a specific trainer by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched trainer profile"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @GetMapping(value = "/{username}")
    public ResponseEntity<TrainerProfileResponse> getTrainerProfile(
            @Parameter(description = "Trainee user name", required = true)
            @PathVariable @NotBlank(message = "Username is required")
            String username) {

        Optional<Trainer> trainerOptional = trainerDBService.selectByUsername(username, login.getPassword());
        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();
            TrainerProfileResponse response = mapTrainerToResponse(trainer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update Trainer Profile", description = "Updates the profile of the logged-in trainer")
    @PutMapping
    public ResponseEntity<TrainerProfileResponse> updateTrainerProfile(
            @Parameter(description = "Trainer profile update request body", required = true)
            @Valid @RequestBody TrainerProfileUpdateRequest request) {
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


    @Operation(summary = "Get trainer trainings list", description = "Retrieves a list of trainings based on filter criteria")
    @GetMapping(value = "/trainings")
    public ResponseEntity<List<TrainingInfo>> getTrainingsList(
            @Parameter(description = "Trainer's username", required = true)
            @RequestParam @NotNull @NotBlank String username,
            @Parameter(description = "Start date filter", required = false)
            @RequestParam(required = false) LocalDate periodFrom,
            @Parameter(description = "End date filter", required = false)
            @RequestParam(required = false) LocalDate periodTo,
            @Parameter(description = "Trainee's name filter", required = false)
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

    @Operation(summary = "Activate/De-Activate Trainer", description = "Changes trainer's active status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active status changed"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input")})
    @PatchMapping(value = "/status")
    public ResponseEntity<String> toggleActiveStatus(
            @Parameter(description = "Trainer active status request body", required = true)
            @Valid @RequestBody ToggleActiveRequest request) {
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
