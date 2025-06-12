package com.waracle.cakemgr.web.controller;

import com.waracle.cakemgr.dto.request.CakeDTO;
import com.waracle.cakemgr.dto.request.CakeServiceRequestDTO;
import com.waracle.cakemgr.dto.response.CakeServiceResponseDTO;
import com.waracle.cakemgr.service.CakeService;
import com.waracle.cakemgr.service.exception.CakeServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * REST controller providing endpoints for managing cakes.
 * <p>
 * Supports create, read, update, and delete (CRUD) operations for cake resources.
 * Secured with role-based access control: administrative actions require ROLE_ADMIN,
 * while retrievals can be accessed by ROLE_ADMIN and ROLE_CUSTOMER.
 * </p>
 */
@Tag(
        name = "Cake Management",
        description = "Provides REST APIs for creating, fetching, updating and deleting cakes"
)
@RestController
@RequestMapping("/cakes")
public class CakeController {

    @Resource
    private final CakeService cakeService;

    /**
     * Constructs the controller with the provided {@link CakeService}.
     *
     * @param cakeService service to manage cake operations
     */
    public CakeController(CakeService cakeService) {
        this.cakeService = cakeService;
    }

    /**
     * Creates a new cake.
     *
     * @param cakeServiceRequestDTO the request payload containing cake details
     * @return response entity containing the persisted cake details or error information
     */
    @PostMapping(
            consumes = { "application/json", "application/xml" },
            produces = { "application/json", "application/xml" }
    )
    @Operation(
            summary = "Create a new cake",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Cake to create",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CakeDTO.class)),
                            @Content(mediaType = "application/xml",  schema = @Schema(implementation = CakeDTO.class))
                    }
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cake created",
                            content = @Content(schema = @Schema(implementation = CakeServiceResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            }
    )
    @Secured({"ROLE_ADMIN"}) // Only ADMIN can create new cakes
    public ResponseEntity<CakeServiceResponseDTO> createCake(@Valid @RequestBody CakeServiceRequestDTO cakeServiceRequestDTO) {
        try {
            return ResponseEntity.ok(new CakeServiceResponseDTO(
                    Collections.singletonList(getCakeDTO(getCakeService().addNewCake(cakeServiceRequestDTO))), null));
        } catch (CakeServiceException cakeServiceException){
            return processCakeServiceException(cakeServiceException);
        }
    }

    /**
     * Retrieves all cakes.
     *
     * @return response entity containing list of all cakes or error information
     */
    @GetMapping(produces = "application/json")
    @Operation(
            summary = "Get all cakes",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "List of cakes",
                    content = @Content(schema = @Schema(implementation = CakeServiceResponseDTO.class))
            )
    )
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER"}) // Both ADMIN and CUSTOMER can fetch cakes
    public ResponseEntity<CakeServiceResponseDTO> getAll() {
        try {
            return ResponseEntity.ok(new CakeServiceResponseDTO(getCakeService().getAllCakes(), null));
        } catch (CakeServiceException cakeServiceException){
            return processCakeServiceException(cakeServiceException);
        }
    }

    /**
     * Retrieves a cake by its ID.
     *
     * @param id the cake identifier
     * @return response entity containing the cake details or 404 if not found
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
            summary = "Get cake by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cake found",
                            content = @Content(schema = @Schema(implementation = CakeServiceResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Cake not found")
            }
    )
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER"})
    public ResponseEntity<CakeServiceResponseDTO> getById(@NotNull @PathVariable Integer id) {
        try {
            CakeDTO cakeDTO = getCakeService().getCake(id);
            return ResponseEntity.ok(new CakeServiceResponseDTO(Collections.singletonList(cakeDTO), null));
        } catch (CakeServiceException cakeServiceException){
            return processCakeServiceException(cakeServiceException);
        }
    }

    /**
     * Updates an existing cake.
     *
     * @param id                   the cake identifier
     * @param cakeServiceRequestDTO the updated cake details
     * @return response entity containing the updated cake or error information
     */
    @PutMapping(
            value = "/{id}",
            consumes = { "application/json", "application/xml" },
            produces = { "application/json", "application/xml" }
    )
    @Operation(
            summary = "Update a cake",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Updated cake data",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CakeDTO.class)),
                            @Content(mediaType = "application/xml",  schema = @Schema(implementation = CakeDTO.class))
                    }
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cake updated",
                            content = @Content(schema = @Schema(implementation = CakeServiceResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Cake not found")
            }
    )
    @Secured({"ROLE_ADMIN"}) // Only ADMIN can update cakes
    public ResponseEntity<CakeServiceResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody CakeServiceRequestDTO cakeServiceRequestDTO) {
        try {
            return ResponseEntity.ok(new CakeServiceResponseDTO(Collections.singletonList(getCakeService().updateCake(id, cakeServiceRequestDTO)), null));
        } catch (CakeServiceException cakeServiceException){
            return processCakeServiceException(cakeServiceException);
        }
    }

    /**
     * Deletes a cake by its ID.
     *
     * @param id the cake identifier
     * @return response entity indicating the result of the deletion operation
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete cake by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Cake not found")
            }
    )
    @Secured({"ROLE_ADMIN"}) // Only ADMIN can delete cakes
    public ResponseEntity<CakeServiceResponseDTO> delete(@NotNull @PathVariable Integer id) {
        try {
            getCakeService().deleteCake(id);
            return ResponseEntity.ok(new CakeServiceResponseDTO(Collections.singletonList(getCakeDTO(id)), null));
        } catch (CakeServiceException cakeServiceException){
            return processCakeServiceException(cakeServiceException);
        }
    }

    /**
     * Returns the {@link CakeService} used by this controller.
     *
     * @return cake service instance
     */
    public CakeService getCakeService() {
        return cakeService;
    }

    /**
     * Processes {@link CakeServiceException} and returns appropriate HTTP responses.
     * <p>
     * Returns 404 Not Found if the exception has no cause, otherwise returns 500 Internal Server Error
     * with the error messages in the response body.
     * </p>
     *
     * @param cakeServiceException the exception to process
     * @return appropriate {@link ResponseEntity} based on the exception details
     */
    private ResponseEntity<CakeServiceResponseDTO> processCakeServiceException (CakeServiceException cakeServiceException){
        if (cakeServiceException.getCause() == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.internalServerError().body(new CakeServiceResponseDTO(cakeServiceException.getErrors()));
        }
    }

    /**
     * Helper method to build a minimal {@link CakeDTO} with only the cake ID set.
     *
     * @param id the cake identifier
     * @return {@link CakeDTO} with the given ID
     */
    private CakeDTO getCakeDTO(Integer id){
        CakeDTO cakeDTO = new CakeDTO();
        cakeDTO.setCakeId(id);
        return cakeDTO;
    }
}
