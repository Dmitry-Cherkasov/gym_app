package com.gym_app.core.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    private static final ThreadLocal<String> transactionId = new ThreadLocal<>();

    // Log method entry for any method in services, with transaction ID
    @Before("execution(* com.gym_app.core.services.*.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        if (transactionId.get() == null) {
            transactionId.set(UUID.randomUUID().toString());
        }
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Transaction ID: {} - Entering method: {} with arguments: {}",
                transactionId.get(), methodName, args);
    }

    // Log method exit for any method in services, with transaction ID
    @AfterReturning(pointcut = "execution(* com.gym_app.core.services.*.*(..))", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String resultString = (result != null) ? result.toString() : "null";

        log.info("Transaction ID: {} - Exiting method: {} with result: {}",
                transactionId.get(), methodName, resultString);
    }

    // Log exceptions thrown by any method in services, with transaction ID
    @AfterThrowing(pointcut = "execution(* com.gym_app.core.services.*.*(..))", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.error("Transaction ID: {} - Exception thrown in method: {} with message: {}",
                transactionId.get(), methodName, exception.getMessage());
    }

    // Clear transaction ID after each transaction completes
    @AfterReturning("execution(* com.gym_app.core.services.*.*(..))")
    public void clearTransactionId() {
        log.info("Ending transaction with ID: {}", transactionId.get());
        transactionId.remove();
    }

    // Accessor for transaction ID
    public static String getTransactionId() {
        return transactionId.get();
    }
}
