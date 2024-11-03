package com.gym_app.core.controller;

import com.gym_app.core.dto.auth.AuthenticationEntity;
import com.gym_app.core.dto.auth.TrainerRegistrationRequest;
import com.gym_app.core.dto.common.ToggleActiveRequest;
import com.gym_app.core.dto.common.Trainee;
import com.gym_app.core.dto.common.Trainer;
import com.gym_app.core.dto.common.Training;
import com.gym_app.core.dto.profile.TrainerProfileResponse;
import com.gym_app.core.dto.profile.TrainerProfileUpdateRequest;
import com.gym_app.core.dto.traininig.TrainingInfo;
import com.gym_app.core.enums.TrainingType;
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
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;



@AutoConfigureMockMvc
class TrainerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrainerDBService trainerDBService;

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private AuthenticationEntity login;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(trainerController, "login", login);

        when(login.getUserName()).thenReturn("user.test");
        when(login.getPassword()).thenReturn("testPassword");
    }

    @Test
    void registerTrainer_ShouldReturnSuccessResponse_WhenValidRequest() throws Exception {
        TrainerRegistrationRequest request = new TrainerRegistrationRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setTrainingType(TrainingType.YOGA);

        Trainer trainer = new Trainer("John", "Doe", "user.test", "testPassword", true, TrainingType.YOGA);
        when(trainerDBService.create(any())).thenReturn(trainer);

        ResponseEntity<Map<String, String >> response = trainerController.registerTrainer(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user.test", response.getBody().get("userName"));
    }

    @Test
    void registerTrainer_ShouldReturnBadRequest_WhenServiceThrowsException() throws Exception {
        when(trainerDBService.create(any())).thenThrow(new RuntimeException("Registration error"));

        ResponseEntity<Map<String, String>> response = trainerController.registerTrainer(new TrainerRegistrationRequest());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void getTrainerProfile_ShouldReturnProfile_WhenTrainerExists() throws Exception {
        Trainer trainer = new Trainer("User", "Test", "user.test", "testPassword", true, TrainingType.YOGA);
        when(trainerDBService.selectByUsername(trainer.getUserName(), trainer.getPassword())).thenReturn(Optional.of(trainer));

        ResponseEntity<TrainerProfileResponse> response = trainerController.getTrainerProfile(trainer.getUserName());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainer.getFirstName(), response.getBody().getFirstName());
    }

    @Test
    void getTrainerProfile_ShouldReturnNotFound_WhenTrainerDoesNotExist() throws Exception {
        when(trainerDBService.selectByUsername(anyString(), anyString())).thenReturn(Optional.empty());

        ResponseEntity<TrainerProfileResponse> response = trainerController.getTrainerProfile("userName");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateTrainerProfile_ShouldReturnUpdatedProfile_WhenSuccessful() throws Exception {
        Trainer oldTrainer = new Trainer("John", "Doe", "user.test", "testPassword", true, TrainingType.YOGA);
        Trainer updatedTrainer = new Trainer("Jack", "Daniels", "user.test", "testPassword", true, TrainingType.YOGA);
        TrainerProfileUpdateRequest request = new TrainerProfileUpdateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setIsActive(true);
        request.setSpecialization(TrainingType.YOGA);

        when(trainerDBService.selectByUsername(anyString(), anyString())).
                thenReturn(Optional.of(oldTrainer)).
                thenReturn(Optional.of(updatedTrainer));
        when(trainerDBService.updateUser(any(), any())).thenReturn(updatedTrainer);

        ResponseEntity<TrainerProfileResponse> response = trainerController.updateTrainerProfile(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTrainer.getFirstName(), response.getBody().getFirstName());
    }

    @Test
    void toggleActiveStatus_ShouldReturnSuccess_WhenStatusChanged() throws Exception {
        // Arrange
        Trainer trainer = new Trainer("John", "Doe", "testUser", "testPassword", true, TrainingType.YOGA);
        ToggleActiveRequest request = new ToggleActiveRequest();
        request.setUsername(trainer.getUserName());
        request.setIsActive(false);

        when(trainerDBService.selectByUsername(request.getUsername(), login.getPassword()))
                .thenReturn(Optional.of(trainer));

        // Act
        ResponseEntity<String> response = trainerController.toggleActiveStatus(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trainerDBService, times(1)).changeStatus(trainer, trainer.getUserName(), trainer.getPassword());
    }

    @Test
    void toggleActiveStatus_ShouldReturnNotFound_WhenTrainerDoesNotExist() throws Exception {
        ToggleActiveRequest request = new ToggleActiveRequest();
        request.setUsername("non_existing_user");
        request.setIsActive(false);

        when(trainerDBService.selectByUsername(anyString(), anyString())).thenReturn(Optional.empty());

        ResponseEntity<String> response = trainerController.toggleActiveStatus(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetTrainingsList_Success() {
        // Arrange
        String username = "testUser";
        LocalDate periodFrom = LocalDate.of(2024, 1, 1);
        LocalDate periodTo = LocalDate.of(2024, 12, 31);
        String traineeName = "John.Doe";
        TrainingType trainingType = TrainingType.ZUMBA;

        Trainee trainee = new Trainee("John", "Doe", "John.Doe", "passwordNoMatter", true, LocalDate.of(2000, 1, 1), "123 Main St");
        Trainer trainer = new Trainer("Green", "Peace", "testUser", "testPassword", true, TrainingType.ZUMBA);

        Training training = new Training();
        training.setTrainingName("ZUMBA training");
        training.setTrainingDate(periodFrom);
        training.setTrainingType(trainingType);
        training.setDuration(60);
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        when(trainerDBService.selectByUsername(username, login.getPassword())).thenReturn(Optional.of(trainer));
        when(trainerDBService.getTrainerTrainings(username, trainer.getPassword(), periodFrom, periodTo, traineeName, trainingType))
                .thenReturn(List.of(training));

        // Act
        ResponseEntity<List<TrainingInfo>> response = trainerController.getTrainingsList(username, periodFrom, periodTo, traineeName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("ZUMBA training", response.getBody().get(0).getTrainingName());
        verify(trainerDBService, times(1)).getTrainerTrainings(username, trainer.getPassword(), periodFrom, periodTo, traineeName, trainingType);
    }
}
