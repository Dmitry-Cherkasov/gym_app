package com.gym_app.core.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // Log method entry for any method in services
    @Before("execution(* com.gym_app.core.services.*.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Entering method: {} with arguments: {}", methodName, args);
    }

    // Log method exit for any method in services
    @AfterReturning(pointcut = "execution(* com.gym_app.core.services.*.*(..))", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String regex = "(?<=password=)[^}]+(?=})";
        log.info("Exiting method: {} with result: {}", methodName, result.toString().replaceAll(regex, "*****"));
    }

    // Log exceptions thrown by any method in services
    @AfterThrowing(pointcut = "execution(* com.gym_app.core.services.*.*(..))", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.error("Exception thrown in method: {} with message: {}", methodName, exception.getMessage());
    }
}
