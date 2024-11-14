package com.gym_app.core.controller;


import com.gym_app.core.dto.auth.AuthenticationEntity;
import com.gym_app.core.dto.auth.ChangeLoginRequest;
import com.gym_app.core.dto.auth.LoginRequest;
import com.gym_app.core.services.TraineeDbService;
import com.gym_app.core.services.TrainerDBService;
import com.gym_app.core.services.JwtTokenProvider;
import com.gym_app.core.util.LoginProtector;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@Validated
@Tag(description = "Authentication management system", name = "Login")
public class LoginController {
    @Autowired
    private AuthenticationEntity authenticationEntity;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private LoginProtector loginProtector;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final TraineeDbService traineeService;
    private final TrainerDBService trainerService;

    @Autowired
    public LoginController(TraineeDbService traineeDbService, TrainerDBService trainerDBService) {
        traineeService = traineeDbService;
        trainerService = trainerDBService;
    }

    @Operation(summary = "Login", description = "User system login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
            @ApiResponse(responseCode = "401", description = "User authentication failed"),
            @ApiResponse(responseCode = "403", description = "User authentication penalty block")
    })
    @PostMapping(value = "/login")
    public ResponseEntity<Map<String,String>> login(
            @RequestBody @Valid LoginRequest loginRequest) {
        String userName = loginRequest.getUserName();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(userName, password));

        if (trainerService.authenticate(userName, password) ||
                traineeService.authenticate(userName, password)) {

            if (!loginProtector.isBlocked(userName))
            {
            authenticationEntity.setUserName(userName);
            authenticationEntity.setPassword(password);
            loginProtector.loginSucceeded(userName);

            String token = jwtTokenProvider.generateToken(userName);

            HashMap<String,String> response = new HashMap<>();
            response.put("token", token);

            return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        loginProtector.loginFailed(userName);
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Operation(summary = "Logout", description = "User system logout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logout success"),
            @ApiResponse(responseCode = "401", description = "Invalid token or user unauthorized")
    })
    @GetMapping(value = "/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization")
                                           @Parameter(description = "Bearer token", required = true)
                                           String token) {

        token = token.substring(7);

        if (!jwtTokenProvider.isValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        jwtTokenProvider.invalidateToken(token);  // Remove token from list;

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "Change login", description = "Changes user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input"),
            @ApiResponse(responseCode = "401", description = "User authentication failed")
    })
    @PutMapping(value = "/login")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "Password change request body", required = true)
            @Valid @RequestBody ChangeLoginRequest request) {
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

    public AuthenticationEntity getAuthenticationEntity() {
        return authenticationEntity;
    }

    @Autowired
    public void setAuthenticationEntity(AuthenticationEntity authenticationEntity) {
        this.authenticationEntity = authenticationEntity;
    }

}
