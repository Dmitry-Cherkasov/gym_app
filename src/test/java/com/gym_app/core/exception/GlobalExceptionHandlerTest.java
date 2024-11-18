package com.gym_app.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gym_app.core.dao.UserJpaDao;
import com.gym_app.core.services.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

@WebMvcTest(GlobalExceptionHandler.class)
@ExtendWith(MockitoExtension.class)
@MockBean(JwtTokenProvider.class)
@MockBean(UserJpaDao.class)
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;
    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(exceptionHandler)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    void testHandleValidationExceptions() throws Exception {
        // Create a BindException to simulate validation errors
        BindException bindException = new BindException(new Object(), "objectName");
        bindException.addError(new FieldError("objectName", "fieldName", "Expected error message"));

        // Create the MethodArgumentNotValidException using the BindException
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindException);

        // Call the handler directly and get the response
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(ex);

        // Verify the status code and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Expected error message", response.getBody().get("fieldName"));
    }

    @Test
    void testHandleEntityNotFoundException() throws Exception {
        // Simulate an EntityNotFoundException
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");

        ResponseEntity<String> response = exceptionHandler.handleEntityNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", response.getBody());
    }

    @Test
    void testHandleIllegalArgumentException() {
        // Simulate an IllegalArgumentException
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

        ResponseEntity<String> response = exceptionHandler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument", response.getBody());
    }

    @Test
    void testHandleGlobalException() {
        // Simulate a general Exception
        Exception ex = new Exception("General error");

        ResponseEntity<String> response = exceptionHandler.handleGlobalException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: General error", response.getBody());
    }
}
