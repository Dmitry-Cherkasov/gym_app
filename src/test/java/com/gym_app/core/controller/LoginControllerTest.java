package com.gym_app.core.controller;

import com.gym_app.core.dto.auth.ChangeLoginRequest;
import com.gym_app.core.dto.auth.LoginRequest;
import com.gym_app.core.services.TraineeDbService;
import com.gym_app.core.services.TrainerDBService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    @Mock
    private TraineeDbService traineeService;

    @Mock
    private TrainerDBService trainerService;


    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(loginController, "authenticationEntity", authenticationEntity);
    }

    @Test
    public void testLogin_Success() {
        // Arrange
        String userName = "testUser";
        String password = "testPassword";
        when(trainerService.authenticate(userName, password)).thenReturn(true);
        when(traineeService.authenticate(userName, password)).thenReturn(false);

        // Act
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName(userName);
        loginRequest.setPassword(password);
        ResponseEntity<Map<String,String>> response = loginController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authenticationEntity).setUserName(userName);
        verify(authenticationEntity).setPassword(password);
    }

    @Test
    public void testLogin_Failure() {
        // Arrange
        String userName = "testUser";
        String password = "wrongPassword";
        when(trainerService.authenticate(userName, password)).thenReturn(false);
        when(traineeService.authenticate(userName, password)).thenReturn(false);

        // Act
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName(userName);
        loginRequest.setPassword(password);
        ResponseEntity<Map<String,String>> response = loginController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authenticationEntity, never()).setUserName(any());
        verify(authenticationEntity, never()).setPassword(any());
    }

    @Test
    public void testChangePassword_SuccessForTrainee() {
        // Arrange
        ChangeLoginRequest request = new ChangeLoginRequest();
        request.setUserName("testUser");
        request.setOldPassword("oldPassword");
        request.setNewPassword("newPassword");
        request.setIsTrainee(true);

        when(traineeService.changePassword(request.getNewPassword(), request.getUserName())).thenReturn(true);

        // Act
        ResponseEntity<Void> response = loginController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authenticationEntity).setPassword("newPassword");
    }

    @Test
    public void testChangePassword_SuccessForTrainer() {
        // Arrange
        ChangeLoginRequest request = new ChangeLoginRequest();
        request.setUserName("testUser");
        request.setOldPassword("oldPassword");
        request.setNewPassword("newPassword");
        request.setIsTrainee(false);

        when(trainerService.changePassword(request.getNewPassword(), request.getUserName())).thenReturn(true);

        // Act
        ResponseEntity<Void> response = loginController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authenticationEntity).setPassword("newPassword");
    }

    @Test
    public void testChangePassword_FailureInvalidCredentials() {
        // Arrange
        ChangeLoginRequest request = new ChangeLoginRequest();
        request.setUserName("testUser");
        request.setOldPassword("oldPassword");
        request.setNewPassword("newPassword");
        request.setIsTrainee(true);

        when(traineeService.changePassword(request.getNewPassword(), request.getUserName())).thenReturn(false);

        // Act
        ResponseEntity<Void> response = loginController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testChangePassword_FailureBadRequest() {
        // Arrange
        ChangeLoginRequest request = new ChangeLoginRequest(); // No fields set

        // Act
        ResponseEntity<Void> response = loginController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testChangePassword_FailureUnexpectedError() {
        // Arrange
        ChangeLoginRequest request = new ChangeLoginRequest();
        request.setUserName("testUser");
        request.setOldPassword("oldPassword");
        request.setNewPassword("newPassword");
        request.setIsTrainee(true);

        when(traineeService.changePassword(request.getNewPassword(), request.getUserName())).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<Void> response = loginController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
