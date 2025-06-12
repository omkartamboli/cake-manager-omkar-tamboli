package com.waracle.cakemgr.service.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exception used to indicate issues detected by implementations of {@link com.waracle.cakemgr.service.CakeService}.
 * <p>
 * This exception encapsulates a list of error messages, allowing detailed error information
 * to be propagated upstream for handling or reporting.
 * </p>
 * <p>
 * It supports multiple constructors to provide flexibility in exception creation,
 * including support for a message, cause, and one or more error messages.
 * </p>
 *
 * @author Omkar Tamboli
 */
public class CakeServiceException extends Exception {

    /**
     * Container to store the errors detected, which can be propagated upstream.
     */
    List<String> errors = new ArrayList<>();

    /**
     * Constructs a new {@code CakeServiceException} with the specified detail message and list of errors.
     *
     * @param message the detail message explaining the exception
     * @param errors  the list of error messages associated with this exception
     */
    public CakeServiceException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    /**
     * Constructs a new {@code CakeServiceException} with the specified detail message, cause, and list of errors.
     *
     * @param message the detail message explaining the exception
     * @param cause   the root cause of the exception
     * @param errors  the list of error messages associated with this exception
     */
    public CakeServiceException(String message, Throwable cause, List<String> errors) {
        super(message, cause);
        this.errors = errors;
    }

    /**
     * Constructs a new {@code CakeServiceException} with the specified single error message and cause.
     *
     * @param error the single error message
     * @param cause the root cause of the exception
     */
    public CakeServiceException(String error, Throwable cause) {
        super(cause);
        this.errors = Collections.singletonList(error);
    }

    /**
     * Returns the list of error messages associated with this exception.
     *
     * @return the list of errors
     */
    public List<String> getErrors() {
        return errors;
    }
}
