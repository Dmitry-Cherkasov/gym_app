package com.gym_app.core.controller;


import com.gym_app.core.dto.auth.AuthenticationEntity;
import com.gym_app.core.dto.auth.ChangeLoginRequest;
import com.gym_app.core.services.TraineeDbService;
import com.gym_app.core.services.TrainerDBService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/login")
@Validated
public class LoginController {
    @Autowired
    private AuthenticationEntity authenticationEntity;
    private final TraineeDbService traineeService;
    private final TrainerDBService trainerService;

    @Autowired
    public LoginController(TraineeDbService traineeDbService, TrainerDBService trainerDBService) {
        traineeService = traineeDbService;
        trainerService = trainerDBService;
    }

    @GetMapping
    public ResponseEntity<Void> login(
            @RequestParam
            @NotBlank(message = "Username is required")
            String userName,
            @NotBlank(message = "Password is required")
            @RequestParam String password) {

        if (trainerService.authenticate(userName, password) || traineeService.authenticate(userName, password)) {
            authenticationEntity.setUserName(userName);
            authenticationEntity.setPassword(password);
            System.out.println(authenticationEntity.getUserName() + " " + authenticationEntity.getPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangeLoginRequest request) {
        String userName = request.getUserName();
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();


        if (userName == null || oldPassword == null || newPassword == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            boolean updated;
            if (request.isTrainee()) {
                updated = traineeService.changePassword(newPassword, userName, oldPassword);
            } else {
                updated = trainerService.changePassword(newPassword, userName, oldPassword);
            }

            if (updated) {
                authenticationEntity.setPassword(newPassword);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (SecurityException e) {
            System.err.println("Authentication failed for user: " + userName);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            System.err.println("Unexpected error during password change for user: " + userName);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
