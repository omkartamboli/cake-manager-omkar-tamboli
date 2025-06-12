package com.waracle.cakemgr.aspect.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution of service and controller methods.
 * <p>
 * This aspect intercepts all methods within the packages
 * {@code com.waracle.cakemgr.service} and
 * {@code com.waracle.cakemgr.web.controller} to log method entry, exit,
 * and exceptions thrown during execution.
 * </p>
 * <p>
 * It uses the following advice types:
 * <ul>
 *   <li>{@code @Before} to log method entry and arguments</li>
 *   <li>{@code @AfterReturning} to log method exit and returned value</li>
 *   <li>{@code @AfterThrowing} to log exceptions thrown by the method</li>
 * </ul>
 * </p>
 *
 * @author Omkar Tamboli
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut that matches all methods in
     * {@code com.waracle.cakemgr.service} and
     * {@code com.waracle.cakemgr.web.controller} packages and their sub-packages.
     */
    @Pointcut("execution(* com.waracle.cakemgr.service..*(..)) || execution(* com.waracle.cakemgr.web.controller..*(..))")
    public void applicationPackages() {}

    /**
     * Advice that logs method entry with method signature and arguments.
     *
     * @param joinPoint provides reflective access to the method being called
     */
    @Before("applicationPackages()")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info("Entering method: {} with args: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    /**
     * Advice that logs method exit with method signature and returned result.
     *
     * @param joinPoint provides reflective access to the method being called
     * @param result the value returned by the method
     */
    @AfterReturning(pointcut = "applicationPackages()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        LOGGER.info("Exiting method: {} with result: {}", joinPoint.getSignature(), result);
    }

    /**
     * Advice that logs exceptions thrown by the method.
     *
     * @param joinPoint provides reflective access to the method being called
     * @param ex the exception thrown by the method
     */
    @AfterThrowing(pointcut = "applicationPackages()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        LOGGER.error("Exception in method: {} - {}", joinPoint.getSignature(), ex.getMessage());
    }
}
