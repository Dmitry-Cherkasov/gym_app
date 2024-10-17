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
public class JpaLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(JpaLoggingAspect.class);

    @Before("execution(* com.gym_app.core.repository.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entering method: " + joinPoint.getSignature().getName());
        logger.info("Arguments: ");
        for (Object arg : joinPoint.getArgs()) {
            logger.info("   " + arg);
        }
    }

    @AfterReturning(value = "execution(* com.gym_app.core.repository.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Exiting method: " + joinPoint.getSignature().getName());
        logger.info("Return value: " + result);
    }

    @AfterThrowing(value = "execution(* com.gym_app.core.repository.*.*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method: " + joinPoint.getSignature().getName());
        logger.error("Exception: " + exception.getMessage(), exception);
    }
}
