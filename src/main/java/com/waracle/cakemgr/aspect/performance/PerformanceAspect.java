package com.waracle.cakemgr.aspect.performance;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for measuring and logging the execution time of controller methods.
 * <p>
 * This aspect intercepts all methods within the
 * {@code com.waracle.cakemgr.web.controller} package and its sub-packages,
 * measuring the time taken to execute each method and logging it.
 * </p>
 *
 * @author Omkar Tamboli
 */
@Aspect
@Component
public class PerformanceAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceAspect.class);

    /**
     * Around advice that logs the execution time of any method in the
     * {@code com.waracle.cakemgr.web.controller} package.
     * <p>
     * It captures the start time before proceeding with the method execution,
     * then calculates the duration after the method completes, and logs
     * the execution time in milliseconds.
     * </p>
     *
     * @param joinPoint the join point providing access to the method execution
     * @return the result of the method execution
     * @throws Throwable if the target method throws any exception
     *
     * @author Omkar Tamboli
     */
    @Around("execution(* com.waracle.cakemgr.web.controller..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed(); // proceed to actual method

        long executionTime = System.currentTimeMillis() - start;

        LOGGER.info(joinPoint.getSignature() + " executed in " + executionTime + "ms");

        return result;
    }
}
