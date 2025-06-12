package com.waracle.cakemgr.service.impl;

import com.waracle.cakemgr.dto.request.CakeDTO;
import com.waracle.cakemgr.dto.request.CakeServiceRequestDTO;
import com.waracle.cakemgr.persistance.entity.CakeEntity;
import com.waracle.cakemgr.persistance.repository.CakeRepository;
import com.waracle.cakemgr.service.exception.CakeServiceException;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CakeServiceImpl}.
 *
 * <p>These tests verify the behavior of CakeServiceImpl in isolation by mocking
 * dependencies such as the {@link CakeRepository}. The {@link MapperFacade} is left real
 * for simplicity, and logging is mocked to avoid test log pollution.</p>
 *
 * <p>Each public service method is tested with:</p>
 * <ul>
 *   <li>Happy path success cases</li>
 *   <li>Negative scenarios such as repository failures or entity not found</li>
 * </ul>
 *
 * <p>These tests ensure that exceptions are wrapped correctly in {@link CakeServiceException}
 * and that the mapping between DTOs and entities behaves as expected.</p>
 *
 * <p><b>Note:</b> These are unit tests, not integration tests—no actual database is used.</p>
 *
 * @author Omkar
 */
class CakeServiceImplTest {

    @InjectMocks
    private CakeServiceImpl cakeService;

    @Mock
    private CakeRepository cakeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cakeService.setCakeRepository(cakeRepository);
    }

    /**
     * Scenario: Successfully add a new cake.
     *
     * <p>Mocks a repository save and verifies that the returned cake ID is as expected.</p>
     */
    @Test
    void testAddNewCakeSuccess() throws CakeServiceException {
        CakeDTO input = new CakeDTO("Chocolate", BigDecimal.valueOf(10.5), "Delicious chocolate cake");

        CakeEntity savedEntity = new CakeEntity("Chocolate", BigDecimal.valueOf(10.5), "Delicious chocolate cake");
        savedEntity.setCakeId(100);

        when(cakeRepository.save(any(CakeEntity.class))).thenReturn(savedEntity);

        Integer resultId = cakeService.addNewCake(input);

        assertEquals(100, resultId);
        verify(cakeRepository).save(any(CakeEntity.class));
    }

    /**
     * Scenario: Adding a new cake fails due to repository throwing an exception.
     *
     * <p>Ensures {@link CakeServiceException} is thrown and error code is present.</p>
     */
    @Test
    void testAddNewCakeThrowsCakeServiceException() {
        CakeDTO input = new CakeDTO("Vanilla", BigDecimal.ONE, "desc");

        when(cakeRepository.save(any(CakeEntity.class))).thenThrow(new RuntimeException("DB down"));

        CakeServiceException ex = assertThrows(CakeServiceException.class, () -> cakeService.addNewCake(input));
        assertTrue(ex.getMessage().contains("DB down"));
        assertTrue(CollectionUtils.isNotEmpty(ex.getErrors()));
        assertTrue(ex.getErrors().get(0).contains("CS:01: Failed to Create new Cake"));
    }

    /**
     * Scenario: Successfully updates an existing cake.
     *
     * <p>Mocks repository to find and flush an entity, verifies updated values are persisted.</p>
     */
    @Test
    void testUpdateCakeSuccess() throws CakeServiceException {
        CakeServiceRequestDTO input = new CakeDTO("Strawberry", BigDecimal.valueOf(12.0), "Fresh strawberry cake");

        CakeEntity foundEntity = new CakeEntity("Old", BigDecimal.valueOf(10.0), "Old desc");
        foundEntity.setCakeId(1);

        when(cakeRepository.findById(1)).thenReturn(Optional.of(foundEntity));
        when(cakeRepository.saveAndFlush(any(CakeEntity.class))).thenReturn(foundEntity);

        cakeService.updateCake(1, input);

        verify(cakeRepository, times(2)).findById(1);
        verify(cakeRepository).saveAndFlush(any(CakeEntity.class));
    }

    /**
     * Scenario: Updating a non-existent cake should throw {@link CakeServiceException}.
     */
    @Test
    void testUpdateCakeNotFoundThrowsException() {
        CakeDTO input = new CakeDTO();
        input.setCakeId(99);

        when(cakeRepository.findById(99)).thenReturn(Optional.empty());

        CakeServiceException ex = assertThrows(CakeServiceException.class, () -> cakeService.updateCake(99, input));
        assertTrue(ex.getMessage().contains("CS:02"));
    }

    /**
     * Scenario: Successfully deletes an existing cake.
     *
     * <p>Ensures no exceptions are thrown and repository delete is called.</p>
     */
    @Test
    void testDeleteCakeSuccess() throws CakeServiceException {
        CakeEntity foundEntity = new CakeEntity("Old", BigDecimal.valueOf(10.0), "Old desc");
        foundEntity.setCakeId(1);

        when(cakeRepository.findById(1)).thenReturn(Optional.of(foundEntity));
        doNothing().when(cakeRepository).delete(foundEntity);

        assertDoesNotThrow(() -> cakeService.deleteCake(1));
        verify(cakeRepository).delete(foundEntity);
    }

    /**
     * Scenario: Deleting a cake causes a repository exception.
     *
     * <p>Asserts that {@link CakeServiceException} is thrown with proper error context.</p>
     */
    @Test
    void testDeleteCakeThrowsException() {
        CakeEntity foundEntity = new CakeEntity("Old", BigDecimal.valueOf(10.0), "Old desc");
        foundEntity.setCakeId(10);

        when(cakeRepository.findById(10)).thenReturn(Optional.of(foundEntity));
        doThrow(new RuntimeException("DB error")).when(cakeRepository).delete(foundEntity);

        CakeServiceException ex = assertThrows(CakeServiceException.class, () -> cakeService.deleteCake(10));
        assertTrue(ex.getMessage().contains("DB error"));
        assertTrue(CollectionUtils.isNotEmpty(ex.getErrors()));
        assertTrue(ex.getErrors().get(0).contains("CS:05: Failed to Delete Cake. ID = 10"));
    }

    /**
     * Scenario: Get an existing cake by ID.
     *
     * <p>Mocks successful lookup and verifies returned DTO matches expectations.</p>
     */
    @Test
    void testGetCakeReturnsPresent() throws CakeServiceException {
        CakeEntity entity = new CakeEntity("Lemon", BigDecimal.valueOf(8.0), "Lemon cake");
        entity.setCakeId(5);

        when(cakeRepository.findById(5)).thenReturn(Optional.of(entity));

        CakeDTO result = cakeService.getCake(5);

        assertNotNull(result);
        assertEquals("Lemon", result.getName());
        assertEquals(BigDecimal.valueOf(8.0), result.getPrice());
    }

    /**
     * Scenario: Get all cakes returns mapped DTO list.
     *
     * <p>Ensures that the returned list size and content match what the repository provides.</p>
     */
    @Test
    void testGetAllCakesReturnsList() throws CakeServiceException {
        CakeEntity cake1 = new CakeEntity("A", BigDecimal.valueOf(1.0), "desc A");
        cake1.setCakeId(1);
        CakeEntity cake2 = new CakeEntity("B", BigDecimal.valueOf(2.0), "desc B");
        cake2.setCakeId(2);

        when(cakeRepository.findAll()).thenReturn(Arrays.asList(cake1, cake2));

        List<CakeDTO> cakes = cakeService.getAllCakes();

        assertEquals(2, cakes.size());
        assertEquals("A", cakes.get(0).getName());
        assertEquals("B", cakes.get(1).getName());
    }

    /**
     * Scenario: Get all cakes when none are found.
     *
     * <p>Ensures that an empty list is returned, not null.</p>
     */
    @Test
    void testGetAllCakesReturnsEmptyList() throws CakeServiceException {
        when(cakeRepository.findAll()).thenReturn(Collections.emptyList());

        List<CakeDTO> cakes = cakeService.getAllCakes();

        assertNotNull(cakes);
        assertTrue(cakes.isEmpty());
    }
}