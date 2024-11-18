package com.gym_app.core.controller;

import com.gym_app.core.dto.auth.TraineeRegistrationRequest;
import com.gym_app.core.dto.common.ToggleActiveRequest;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.dto.common.Trainee;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.dto.profile.TraineeProfileResponse;
import com.gym_app.core.dto.profile.TraineeProfileUpdateRequest;
import com.gym_app.core.dto.profile.TrainerSummary;
import com.gym_app.core.dto.profile.TrainersListUpdateRequest;
import com.gym_app.core.dto.traininig.TrainingCreateRequest;
import com.gym_app.core.dto.traininig.TrainingInfo;
import com.gym_app.core.enums.TrainingType;
import com.gym_app.core.services.JwtTokenProvider;
import com.gym_app.core.services.TraineeDbService;
import com.gym_app.core.services.TrainerDBService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
public class TraineeControllerTest {

    @Mock
    private TraineeDbService traineeService;

    @Mock
    private TrainerDBService trainerService;

    @Mock
    JwtTokenProvider jwtTokenProvider;


    @InjectMocks
    private TraineeController traineeController;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(traineeController, "jwtTokenProvider", jwtTokenProvider);

    }

    @Test
    public void testRegisterTrainee_Success() {
        // Arrange
        TraineeRegistrationRequest request = new TraineeRegistrationRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setBirthDate(LocalDate.of(2000, 1, 1));
        request.setAddress("123 Main St");

        Trainee trainee = new Trainee("John", "Doe", "", "", true, LocalDate.of(2000, 1, 1), "123 Main St");
        trainee.setUserName("johndoe");
        trainee.setPassword("securepassword");

        when(traineeService.create(any(Trainee.class))).thenReturn(trainee);

        ResponseEntity<Map<String, String>> response = traineeController.registerTrainee(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("johndoe", response.getBody().get("userName"));
        assertEquals("securepassword", response.getBody().get("password"));
    }

    @Test
    public void testRegisterTrainee_Fail() {
        // Arrange
        TraineeRegistrationRequest request = new TraineeRegistrationRequest();
        request.setFirstName("J");
        request.setLastName("D");
        request.setBirthDate(LocalDate.of(2000, 1, 1));
        request.setAddress("123 Main St");

        when(traineeService.create(any(Trainee.class))).thenThrow(new RuntimeException());

        ResponseEntity<Map<String, String>> response = traineeController.registerTrainee(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

    @Test
    public void testGetTraineeProfile_Success() {
        // Arrange
        Trainee trainee = new Trainee("John", "Doe", "testUser", "password", true, LocalDate.of(2000, 1, 1), "123 Main St");
        when(traineeService.selectByUsername("testUser"
        )).thenReturn(Optional.of(trainee));

        // Act
        ResponseEntity<TraineeProfileResponse> response = traineeController.getTraineeProfile("testUser");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testUser", response.getBody().getUserName());
    }

    @Test
    public void testUpdateTraineeProfile_Success() {
        // Arrange
        Trainee oldTrainee = new Trainee("John", "Doe", "testUser", "testPassword", true, LocalDate.of(2000, 1, 1), "123 Main St");
        Trainee updatedTrainee = new Trainee("Jill", "Eod", "testUser", "testPassword", true, LocalDate.of(2000, 1, 1), "Updated Address");

        // Set up the request with updated data
        TraineeProfileUpdateRequest request = new TraineeProfileUpdateRequest();
        request.setFirstName(updatedTrainee.getFirstName());
        request.setLastName(updatedTrainee.getLastName());
        request.setUserName(updatedTrainee.getUserName());
        request.setDateOfBirth(updatedTrainee.getDateOfBirth());
        request.setAddress(updatedTrainee.getAddress());
        request.setIsActive(updatedTrainee.isActive());

        // Mock the selectByUsername calls
        when(traineeService.selectByUsername("testUser"))
                .thenReturn(Optional.of(oldTrainee)).
                thenReturn(Optional.of(updatedTrainee));

        // Act - Call updateTraineeProfile
        ResponseEntity<TraineeProfileResponse> response = traineeController.updateTraineeProfile(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testUser", response.getBody().getUserName());
        assertEquals("Updated Address", response.getBody().getAddress()); // Confirm the updated address

    }

    @Test
    public void testDeleteTraineeProfile_Success() {
        // Arrange
        when(traineeService.delete("johndoe")).thenReturn(true);

        // Act
        ResponseEntity<Void> response = traineeController.deleteTraineeProfile("johndoe");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAvailableTrainers_Success() {
        // Arrange
        Trainee trainee = new Trainee("John", "Doe", "testUser", "testPassword", true, LocalDate.of(2000, 1, 1), "123 Main St");
        when(traineeService.selectByUsername("testUser")).thenReturn(Optional.of(trainee));
        Trainer trainer1 = new Trainer("Arvan", "Stern", "Ar.Stern", "password12345", true, TrainingType.FITNESS);
        Trainer trainer2 = new Trainer("Green", "Peace", "GreenP", "password12345678", true, TrainingType.ZUMBA);
        when(traineeService.getAvailableTrainers("testUser")).thenReturn(Arrays.asList(trainer1, trainer2));

        // Act
        ResponseEntity<List<TrainerSummary>> response = traineeController.getAvailableTrainers("testUser");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testUpdateTraineeTrainerList_Success() {
        // Arrange testPassword
        String traineeUsername = "testUser";
        String trainerUsername1 = "trainer1";
        String trainerUsername2 = "trainer2";

        Trainee trainee = new Trainee("Trainee", "Test", traineeUsername, "testPassword", true, LocalDate.now(), "Address");
        Trainer trainer1 = new Trainer("Arvan", "Stern", trainerUsername1, "password12345", true, TrainingType.FITNESS);
        Trainer trainer2 = new Trainer("Green", "Peace", trainerUsername2, "password12345678", true, TrainingType.ZUMBA);

        TrainersListUpdateRequest request = new TrainersListUpdateRequest();
        request.setTraineeUsername(traineeUsername);
        request.setTrainersList(List.of(trainerUsername1, trainerUsername2));

        when(traineeService.selectByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        when(traineeService.getAvailableTrainers(anyString())).thenReturn(List.of(trainer1, trainer2));

        doNothing().when(traineeService).addTrainerToList(anyString(), any(Trainer.class));

        // Act
        ResponseEntity<List<TrainerSummary>> response = traineeController.updateTraineeTrainerList(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(trainerUsername1, response.getBody().get(0).getUsername());
        assertEquals(trainerUsername2, response.getBody().get(1).getUsername());
    }

    @Test
    public void testGetTrainingsList_Success() {
        // Arrange
        String username = "testUser";
        LocalDate periodFrom = LocalDate.of(2024, 1, 1);
        LocalDate periodTo = LocalDate.of(2024, 12, 31);
        String trainerName = "GreenP";
        TrainingType trainingType = TrainingType.ZUMBA;

        Trainee trainee = new Trainee("John", "Doe", "testUser", "testPassword", true, LocalDate.of(2000, 1, 1), "123 Main St");
        Trainer trainer = new Trainer("Green", "Peace", "GreenP", "password12345678", true, TrainingType.ZUMBA);

        Training training = new Training();
        training.setTrainingName("ZUMBA training");
        training.setTrainingDate(periodFrom);
        training.setTrainingType(trainingType);
        training.setDuration(60);
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        when(traineeService.selectByUsername(username)).thenReturn(Optional.of(trainee));
        when(traineeService.getTraineeTrainings(username, periodFrom, periodTo, trainerName, trainingType))
                .thenReturn(List.of(training));

        // Act
        ResponseEntity<List<TrainingInfo>> response = traineeController.getTrainingsList(username, periodFrom, periodTo, trainerName, trainingType);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("ZUMBA training", response.getBody().get(0).getTrainingName());
        verify(traineeService, times(1)).getTraineeTrainings(username, periodFrom, periodTo, trainerName, trainingType);
    }

    @Test
    public void testAddTraining_Success() {
        // Arrange
        Trainee trainee = new Trainee("John", "Doe", "testUser", "testPassword", true, LocalDate.of(2000, 1, 1), "123 Main St");
        when(traineeService.selectByUsername("testUser")).thenReturn(Optional.of(trainee));

        Trainer trainer = new Trainer("Green", "Peace", "GreenP", "password12345678", true, TrainingType.ZUMBA);
        TrainingCreateRequest trainingRequest = new TrainingCreateRequest();
        trainingRequest.setTraineeUsername("testUser");
        trainingRequest.setTrainerUsername(trainer.getUserName());
        trainingRequest.setTrainingName("Yoga Session");
        trainingRequest.setTrainingDate(LocalDate.now());
        trainingRequest.setTrainingDuration(60);

        when(traineeService.getAvailableTrainers("testUser")).thenReturn(Arrays.asList(trainer));

        // Act
        ResponseEntity<String> response = traineeController.addTraining(trainingRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Training successfully added", response.getBody());
    }

    @Test
    public void testToggleActiveStatus_Success() {
        // Arrange
        Trainee trainee = new Trainee("John", "Doe", "testUser", "testPassword", true, LocalDate.of(2000, 1, 1), "123 Main St");
        ToggleActiveRequest request = new ToggleActiveRequest();
        request.setUsername(trainee.getUserName());
        request.setIsActive(false);

        when(traineeService.selectByUsername(request.getUsername()))
                .thenReturn(Optional.of(trainee));

        // Act
        ResponseEntity<String> response = traineeController.toggleActiveStatus(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(traineeService, times(1)).changeStatus(trainee, trainee.getUserName());
    }

    @Test
    public void testToggleActiveStatus_TraineeNotFound() {
        // Arrange
        ToggleActiveRequest request = new ToggleActiveRequest();
        request.setUsername("nonexistentUser");
        request.setIsActive(false);
        when(traineeService.selectByUsername(request.getUsername()))
                .thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = traineeController.toggleActiveStatus(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trainee not found", response.getBody());
        verify(traineeService, never()).changeStatus(any(), anyString());
    }
}
