package com.gym_app.core.controller;


import com.gym_app.core.dto.auth.ChangeLoginRequest;
import com.gym_app.core.dto.auth.LoginRequest;
import com.gym_app.core.services.TraineeDbService;
import com.gym_app.core.services.TrainerDBService;
import com.gym_app.core.services.JwtTokenProvider;
import com.gym_app.core.util.LoginProtector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LoginProtector loginProtector;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TraineeDbService traineeDbService;

    @Mock
    private TrainerDBService trainerDbService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        ReflectionTestUtils.setField(loginController, "jwtTokenProvider", jwtTokenProvider);
        ReflectionTestUtils.setField(loginController, "loginProtector", loginProtector);
        ReflectionTestUtils.setField(loginController, "authenticationManager", authenticationManager);

    }

    @Test
    void testLogin_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("testUser");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        authentication.setAuthenticated(true);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword())))
                .thenReturn(authentication); // Simulate successful authentication

        when(jwtTokenProvider.generateToken("testUser")).thenReturn("token123");

        ResponseEntity<Map<String,String>> response = loginController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        }


    @Test
    void testLogin_Failure() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("testUser");
        loginRequest.setPassword("wrongPass");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        ResponseEntity<Map<String,String>> response = loginController.login(loginRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    void testLogout_Success() throws Exception {
        String token = "Bearer token123";

        when(jwtTokenProvider.isValid(anyString())).thenReturn(true);

        mockMvc.perform(get("/logout")
                        .header("Authorization", token))
                .andExpect(status().isOk());

        verify(jwtTokenProvider, times(1)).invalidateToken(anyString());
    }

    @Test
    void testLogout_Failure_InvalidToken() throws Exception {
        String token = "Bearer invalidToken123";

        when(jwtTokenProvider.isValid(anyString())).thenReturn(false);

        mockMvc.perform(get("/logout")
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized());

        verify(jwtTokenProvider, times(0)).invalidateToken(anyString());
    }

    @Test
    void testChangePassword_Success() throws Exception {
        ChangeLoginRequest changeLoginRequest = new ChangeLoginRequest();
        changeLoginRequest.setUserName("testUser");
        changeLoginRequest.setOldPassword("oldPassword");
        changeLoginRequest.setNewPassword("newPassword");
        changeLoginRequest.setIsTrainee(false);

        when(trainerDbService.changePassword(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(put("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"testUser\",\"oldPassword\":\"oldPassword\",\"newPassword\":\"newPassword\",\"isTrainee\":false}"))
                .andExpect(status().isOk());

        verify(trainerDbService, times(1)).changePassword(anyString(), anyString());
    }

    @Test
    void testChangePassword_Failure() throws Exception {
        ChangeLoginRequest changeLoginRequest = new ChangeLoginRequest();
        changeLoginRequest.setUserName("testUser");
        changeLoginRequest.setOldPassword("oldPassword");
        changeLoginRequest.setNewPassword("newPassword");
        changeLoginRequest.setIsTrainee(false);


        when(trainerDbService.changePassword(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(put("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"testUser\",\"oldPassword\":\"wrongOldPassword\",\"newPassword\":\"newPassword\",\"isTrainee\":false}"))
                .andExpect(status().isUnauthorized());

        verify(trainerDbService, times(1)).changePassword(anyString(), anyString());
    }
}
