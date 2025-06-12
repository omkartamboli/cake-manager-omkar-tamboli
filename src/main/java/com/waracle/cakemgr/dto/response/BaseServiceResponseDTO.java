package com.waracle.cakemgr.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for service response DTOs that includes support for error messages.
 * This class can be extended by other DTOs to include a standard error response format.
 *
 * @author Omkar Tamboli
 */
public class BaseServiceResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * A list of error messages associated with the service response.
     */
    private List<String> errors = new ArrayList<>();

    /**
     * Retrieves the list of error messages.
     *
     * @return a list of error messages
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Sets the list of error messages.
     *
     * @param errors a list of error messages
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    /**
     * Constructs a new {@code BaseServiceResponseDTO} with a list of error messages.
     *
     * @param errors a list of error messages
     */
    public BaseServiceResponseDTO(List<String> errors) {
        this.errors = errors;
    }

    /**
     * Default constructor for creating an empty {@code BaseServiceResponseDTO}.
     */
    public BaseServiceResponseDTO() {
    }
}
