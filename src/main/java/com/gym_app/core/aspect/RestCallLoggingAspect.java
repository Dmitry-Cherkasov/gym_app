package com.gym_app.core.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class RestCallLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(RestCallLoggingAspect.class);

    public RestCallLoggingAspect() {
        log.info("RestCallLoggingAspect instantiated");
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logRestCall(ProceedingJoinPoint joinPoint) throws Throwable {
        // Retrieve the current HTTP request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String endpoint = request.getRequestURI();
        String httpMethod = request.getMethod();

        // Log the incoming request details
        logIncomingRequest(httpMethod, endpoint, joinPoint.getArgs());

        try {
            // Proceed with the method execution
            Object response = joinPoint.proceed();
            // Log the response details
            logResponse(endpoint, response);
            return response;
        } catch (Exception e) {
            // Log any exception that occurs during method execution
            logError(endpoint, e);
            throw e;
        }
    }

    private void logIncomingRequest(String httpMethod, String endpoint, Object[] args) {
        log.info("Incoming request: {} {} with data: {}", httpMethod, endpoint, args);
    }

    private void logResponse(String endpoint, Object response) {
        log.info("Response from {}: {}", endpoint, response);
    }

    private void logError(String endpoint, Exception e) {
        log.error("Error in {}: {}", endpoint, e.getMessage(), e);
    }
}