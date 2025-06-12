package com.waracle.cakemgr.service.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST controllers.
 * <p>
 * This class handles exceptions thrown by controller methods and returns appropriate HTTP responses.
 * Currently, it handles {@link MethodArgumentNotValidException} to return validation error details.
 * </p>
 *
 * <p>
 * Validation errors are collected and returned as a list of error messages in the response body
 * with HTTP status 400 (Bad Request).
 * </p>
 *
 * @author Omkar Tamboli
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions thrown when method arguments fail validation constraints.
     *
     * @param ex the exception thrown when validation fails
     * @return a {@link ResponseEntity} containing a list of field error messages and HTTP 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }
}
