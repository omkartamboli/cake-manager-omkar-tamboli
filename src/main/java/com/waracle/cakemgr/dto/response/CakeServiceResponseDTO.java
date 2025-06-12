package com.waracle.cakemgr.dto.response;

import com.waracle.cakemgr.dto.request.CakeDTO;

import java.util.List;

/**
 * Represents a response DTO for cake-related service operations.
 * Extends {@link BaseServiceResponseDTO} to include error messages if any.
 * Contains a list of {@link CakeDTO} objects as the response payload.
 *
 * @author Omkar Tamboli
 */
public class CakeServiceResponseDTO extends BaseServiceResponseDTO {

    /**
     * A list of cake DTOs returned by the service.
     */
    private List<CakeDTO> cakes;

    /**
     * Gets the list of cakes in the response.
     *
     * @return list of {@link CakeDTO}
     */
    public List<CakeDTO> getCakes() {
        return cakes;
    }

    /**
     * Sets the list of cakes for the response.
     *
     * @param cakes list of {@link CakeDTO} to set
     */
    public void setCakes(List<CakeDTO> cakes) {
        this.cakes = cakes;
    }

    /**
     * Constructs a new response DTO with a list of cakes and error messages.
     *
     * @param cakes  the list of cakes returned
     * @param errors the list of error messages, if any
     */
    public CakeServiceResponseDTO(List<CakeDTO> cakes, List<String> errors) {
        super(errors);
        this.cakes = cakes;
    }

    /**
     * Constructs a new response DTO with only error messages.
     *
     * @param errors the list of error messages
     */
    public CakeServiceResponseDTO(List<String> errors) {
        super(errors);
    }
}
