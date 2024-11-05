package com.gym_app.core.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestCallLoggingAspectTest {

    private RestCallLoggingAspect aspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        aspect = new RestCallLoggingAspect();
        // Set up the mock request context
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testLogRestCall_Success() throws Throwable {
        // Arrange
        String endpoint = "/api/test";
        String httpMethod = "GET";
        Object[] args = {"arg1", "arg2"};
        Object response = "Success";

        when(request.getRequestURI()).thenReturn(endpoint);
        when(request.getMethod()).thenReturn(httpMethod);
        when(joinPoint.proceed()).thenReturn(response);

        // Act
        Object result = aspect.logRestCall(joinPoint);

        // Assert
        assertEquals(response, result);
        verify(joinPoint).proceed();
        // Here you would verify log statements if needed
    }

    @Test
    public void testLogRestCall_Error() throws Throwable {
        // Arrange
        String endpoint = "/api/test";
        String httpMethod = "GET";
        Exception exception = new RuntimeException("Test exception");

        when(request.getRequestURI()).thenReturn(endpoint);
        when(request.getMethod()).thenReturn(httpMethod);
        when(joinPoint.proceed()).thenThrow(exception);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> aspect.logRestCall(joinPoint));
        verify(joinPoint).proceed();
        // Here you would verify log statements for errors if needed
    }

    @Test
    public void testLogIncomingRequest() {
        // You can add tests for private methods using reflection if necessary,
        // or just ensure the logging is called in the public method.
    }

    @Test
    public void testLogResponse() {
        // Same as above
    }

    @Test
    public void testLogError() {
        // Same as above
    }
}
