package com.waracle.cakemgr.service.impl;

import com.waracle.cakemgr.dto.request.CakeDTO;
import com.waracle.cakemgr.dto.request.CakeServiceRequestDTO;
import com.waracle.cakemgr.persistance.entity.CakeEntity;
import com.waracle.cakemgr.persistance.repository.CakeRepository;
import com.waracle.cakemgr.service.CakeService;
import com.waracle.cakemgr.service.exception.CakeServiceException;
import jakarta.annotation.Resource;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of the {@link CakeService} interface providing
 * CRUD operations for Cake entities.
 * <p>
 * This service handles creation, retrieval, update, and deletion of cakes
 * via the {@link CakeRepository}, while mapping between DTOs and entities.
 * </p>
 * <p>
 * It uses Orika {@link MapperFacade} for DTO-entity mapping and logs
 * exceptions encountered during data operations.
 * </p>
 * <p>
 * All the APIs from service try-catch {@link RuntimeException} to cover unexpected failures
 * from persistence layer.
 *
 * <p>Common {@link RuntimeException}s that may be thrown by {@link org.springframework.data.repository.CrudRepository} methods:</p>
 *
 * <ul>
 *   <li>{@link IllegalArgumentException} – if null or invalid arguments are passed to repository methods</li>
 *   <li>{@link org.springframework.dao.DataIntegrityViolationException} – if a database constraint (e.g. unique, not-null) is violated</li>
 *   <li>{@link org.springframework.orm.jpa.JpaObjectRetrievalFailureException} – if an entity reference cannot be resolved</li>
 *   <li>{@link org.springframework.dao.InvalidDataAccessApiUsageException} – for improper usage of the JPA API or detached entities</li>
 *   <li>{@link jakarta.persistence.TransactionRequiredException} – if a modifying operation is attempted without a transaction</li>
 *   <li>{@link org.springframework.orm.ObjectOptimisticLockingFailureException} – if optimistic locking fails (e.g. stale version conflict)</li>
 *   <li>{@link jakarta.persistence.QueryTimeoutException} – if a query exceeds the defined timeout limit</li>
 *   <li>{@link jakarta.persistence.PersistenceException} – for generic JPA provider failures (usually wrapped by Spring exceptions)</li>
 *   <li>{@link org.springframework.core.convert.ConversionFailedException} – if a query parameter or result cannot be converted</li>
 * </ul>
 *
 * @author Omkar Tamboli
 */
@Service
public class CakeServiceImpl implements CakeService {

    public static final String CAKE_DOES_NOT_EXIST = "Cake does not exist for id : ";

    @Resource
    private CakeRepository cakeRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CakeServiceImpl.class);

    private static final MapperFacade mapper = new DefaultMapperFactory.Builder().build().getMapperFacade();

    /**
     * Adds a new cake to the repository.
     *
     * @param cake the {@link CakeServiceRequestDTO} containing cake details
     * @return the ID of the newly created cake
     * @throws CakeServiceException if the creation fails due to repository errors
     */
    @Override
    public Integer addNewCake(CakeServiceRequestDTO cake) throws CakeServiceException {
        try {
            CakeEntity cakeEntity = getCakeRepository().save(new CakeEntity(cake.getName(), cake.getPrice(), cake.getDescription()));
            return cakeEntity.getCakeId();
        } catch (RuntimeException runtimeException) {
            LOGGER.error(runtimeException.getMessage(), runtimeException);
            throw new CakeServiceException("CS:01: Failed to Create new Cake", runtimeException);
        }
    }

    /**
     * Updates an existing cake identified by the given ID.
     *
     * @param id   the ID of the cake to update
     * @param cake the {@link CakeServiceRequestDTO} containing updated cake details
     * @return the updated {@link CakeDTO}
     * @throws CakeServiceException if the cake does not exist or update fails
     */
    @Override
    public CakeDTO updateCake(Integer id, CakeServiceRequestDTO cake) throws CakeServiceException {
        try {
            cakeRepository.findById(id)
                    .orElseThrow(() -> new CakeServiceException("CS:02: Failed to Update Cake. ID = " + id, Collections.singletonList(CAKE_DOES_NOT_EXIST + id)));

            getCakeRepository().saveAndFlush(mapper.map(getCakeDtoFromRequest(id, cake), CakeEntity.class));

            CakeEntity cakeEntity = cakeRepository.findById(id)
                    .orElseThrow(() -> new CakeServiceException("CS:02: Failed to Update Cake. ID = " + id, Collections.singletonList(CAKE_DOES_NOT_EXIST + id)));

            return mapper.map(cakeEntity, CakeDTO.class);

        } catch (RuntimeException runtimeException) {
            LOGGER.error(runtimeException.getMessage(), runtimeException);
            throw new CakeServiceException("CS:03: Failed to Update Cake. ID = " + id, runtimeException);
        }
    }

    /**
     * Deletes a cake identified by the given ID.
     *
     * @param id the ID of the cake to delete
     * @throws CakeServiceException if the cake does not exist or deletion fails
     */
    @Override
    public void deleteCake(Integer id) throws CakeServiceException {
        try {
            CakeEntity entity = cakeRepository.findById(id)
                    .orElseThrow(() -> new CakeServiceException("CS:04: Failed to Delete Cake. ID = " + id, Collections.singletonList(CAKE_DOES_NOT_EXIST + id)));
            getCakeRepository().delete(entity);
        } catch (RuntimeException runtimeException) {
            LOGGER.error(runtimeException.getMessage(), runtimeException);
            throw new CakeServiceException("CS:05: Failed to Delete Cake. ID = " + id, runtimeException);
        }
    }

    /**
     * Retrieves a cake by its ID.
     *
     * @param id the ID of the cake to retrieve
     * @return the {@link CakeDTO} representing the cake
     * @throws CakeServiceException if the cake is not found or retrieval fails
     */
    @Override
    public CakeDTO getCake(Integer id) throws CakeServiceException {
        try {
            CakeEntity cakeEntity = cakeRepository.findById(id)
                    .orElseThrow(() -> new CakeServiceException("CS:06: Failed to find Cake. ID = " + id, Collections.singletonList(CAKE_DOES_NOT_EXIST + id)));

            return mapper.map(cakeEntity, CakeDTO.class);
        } catch (RuntimeException runtimeException) {
            LOGGER.error(runtimeException.getMessage(), runtimeException);
            throw new CakeServiceException("CS:07: Failed to find Cake. ID = " + id, runtimeException);
        }
    }

    /**
     * Retrieves all cakes from the repository.
     *
     * @return list of {@link CakeDTO} representing all cakes
     * @throws CakeServiceException if retrieval fails
     */
    @Override
    public List<CakeDTO> getAllCakes() throws CakeServiceException {
        return getCakeRepository()
                .findAll()
                .stream()
                .map(cakeEntity -> mapper.map(cakeEntity, CakeDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Helper method to map a {@link CakeServiceRequestDTO} and ID to a {@link CakeDTO}.
     *
     * @param id                    the cake ID
     * @param cakeServiceRequestDTO the cake request data transfer object
     * @return a new {@link CakeDTO} populated with the provided data
     */
    private CakeDTO getCakeDtoFromRequest(Integer id, CakeServiceRequestDTO cakeServiceRequestDTO) {
        CakeDTO cakeDTO = new CakeDTO();
        cakeDTO.setName(cakeServiceRequestDTO.getName());
        cakeDTO.setPrice(cakeServiceRequestDTO.getPrice());
        cakeDTO.setDescription(cakeServiceRequestDTO.getDescription());
        cakeDTO.setCakeId(id);
        return cakeDTO;
    }

    public CakeRepository getCakeRepository() {
        return cakeRepository;
    }

    public void setCakeRepository(CakeRepository cakeRepository) {
        this.cakeRepository = cakeRepository;
    }
}
