package com.waracle.cakemgr.service;

import com.waracle.cakemgr.dto.request.CakeDTO;
import com.waracle.cakemgr.dto.request.CakeServiceRequestDTO;
import com.waracle.cakemgr.service.exception.CakeServiceException;

import java.util.List;

/**
 * Service interface defining operations for managing cakes.
 * <p>
 * Provides methods for creating, updating, deleting, and retrieving cake data.
 * All methods may throw {@link CakeServiceException} to indicate service-level errors.
 * </p>
 *
 * @author Omkar Tamboli
 */
public interface CakeService {

    /**
     * Creates a new cake record.
     *
     * @param cake the {@link CakeServiceRequestDTO} containing details of the cake to create
     * @return the ID of the newly created cake
     * @throws CakeServiceException if creation fails
     */
    Integer addNewCake(CakeServiceRequestDTO cake) throws CakeServiceException;

    /**
     * Updates an existing cake by ID.
     *
     * @param id   the ID of the cake to update
     * @param cake the {@link CakeServiceRequestDTO} containing updated cake details
     * @return the updated {@link CakeDTO}
     * @throws CakeServiceException if update fails or cake does not exist
     */
    CakeDTO updateCake(Integer id, CakeServiceRequestDTO cake) throws CakeServiceException;

    /**
     * Deletes a cake by its ID.
     *
     * @param id the ID of the cake to delete
     * @throws CakeServiceException if deletion fails or cake does not exist
     */
    void deleteCake(Integer id) throws CakeServiceException;

    /**
     * Retrieves a cake by its ID.
     *
     * @param id the ID of the cake to retrieve
     * @return the {@link CakeDTO} representing the cake
     * @throws CakeServiceException if retrieval fails or cake does not exist
     */
    CakeDTO getCake(Integer id) throws CakeServiceException;

    /**
     * Retrieves all cakes.
     *
     * @return a list of {@link CakeDTO} representing all cakes
     * @throws CakeServiceException if retrieval fails
     */
    List<CakeDTO> getAllCakes() throws CakeServiceException;
}
