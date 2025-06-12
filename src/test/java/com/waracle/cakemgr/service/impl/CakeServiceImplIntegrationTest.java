package com.waracle.cakemgr.service.impl;

import com.waracle.cakemgr.dto.request.CakeDTO;
import com.waracle.cakemgr.dto.request.CakeServiceRequestDTO;
import com.waracle.cakemgr.persistance.entity.CakeEntity;
import com.waracle.cakemgr.persistance.repository.CakeRepository;
import com.waracle.cakemgr.service.CakeService;
import com.waracle.cakemgr.service.exception.CakeServiceException;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Integration tests for {@link CakeServiceImpl}.
 *
 * <p>This test class verifies the end-to-end behavior of the {@link CakeService} implementation
 * including persistence logic, entity-to-DTO mapping, and error handling.</p>
 *
 * <p>It uses Spring Boot's testing support to bootstrap the full application context and runs
 * against an in-memory HSQLDB database (auto-configured for tests). Each test is annotated with
 * {@link jakarta.transaction.Transactional} to ensure isolation by rolling back changes after each test.</p>
 *
 * <p>The tests cover core service operations:</p>
 * <ul>
 *     <li>Adding a new cake and verifying it is persisted.</li>
 *     <li>Updating a cake and validating changes in DB and response.</li>
 *     <li>Handling updates to non-existent records by throwing exceptions.</li>
 *     <li>Deleting cakes and confirming deletion from repository.</li>
 *     <li>Fetching individual and all cakes, verifying mappings.</li>
 * </ul>
 *
 * <p>A test-specific {@link MapperFacade} bean is injected for object mapping using Orika.</p>
 *
 * @author Omkar Tamboli
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@Transactional
@ComponentScan(basePackages = "com.waracle.cakemgr")
class CakeServiceImplIntegrationTest {

    @Resource
    private CakeService cakeService;

    @Resource
    private CakeRepository cakeRepository;

    /**
     * Helper method to construct a {@link CakeDTO}.
     */
    private CakeDTO buildCakeDTO(String name, double price, String description) {
        CakeDTO dto = new CakeDTO();
        dto.setName(name);
        dto.setPrice(BigDecimal.valueOf(price));
        dto.setDescription(description);
        return dto;
    }

    /**
     * Test: Add a new cake and verify it is saved to the repository with a generated ID.
     */
    @Test
    @DisplayName("addNewCake – successfully saves and returns generated ID")
    void addNewCake_success() throws CakeServiceException {
        CakeDTO dto = buildCakeDTO("Chocolate", 9.99, "Rich chocolate cake");

        Integer id = cakeService.addNewCake(dto);

        assertNotNull(id);
        assertTrue(cakeRepository.findById(id).isPresent());
    }

    /**
     * Test: Update an existing cake and verify both DB persistence and returned DTO match expectations.
     */
    @Test
    @DisplayName("updateCake – updates existing row and echoes DTO")
    void updateCake_success() throws CakeServiceException {
        CakeEntity base = new CakeEntity("Vanilla", BigDecimal.valueOf(5.0), "vanilla");
        cakeRepository.save(base);

        CakeServiceRequestDTO update = buildCakeDTO("Vanilla Deluxe", 7.0, "updated");
        CakeDTO result = cakeService.updateCake(base.getCakeId(), update);

        CakeEntity fromDb = cakeRepository.findById(base.getCakeId()).orElseThrow();
        assertEquals("Vanilla Deluxe", fromDb.getName());
        assertEquals(BigDecimal.valueOf(7.0), fromDb.getPrice());
        assertEquals(result.getName(), update.getName());
    }

    /**
     * Test: Attempt to update a non-existent cake and assert that {@link CakeServiceException} is thrown.
     */
    @Test
    @DisplayName("updateCake – throws when ID not found")
    void updateCake_notFound() {
        CakeDTO missing = buildCakeDTO("Missing", 1, "none");
        missing.setCakeId(999);

        CakeServiceException ex = assertThrows(CakeServiceException.class,
                () -> cakeService.updateCake(999, missing));
        assertTrue(ex.getMessage().contains("CS:02"));
    }

    /**
     * Test: Delete an existing cake and ensure it is removed from the repository.
     */
    @Test
    @DisplayName("deleteCake – removes row")
    void deleteCake_success() throws CakeServiceException {
        CakeEntity entity = cakeRepository.save(
                new CakeEntity("Temp", BigDecimal.ONE, "tmp"));

        cakeService.deleteCake(entity.getCakeId());

        assertFalse(cakeRepository.findById(entity.getCakeId()).isPresent());
    }

    /**
     * Test: Retrieve a cake by ID and verify the returned DTO matches the persisted data.
     */
    @Test
    @DisplayName("getCake – returns DTO when present")
    void getCake_present() throws CakeServiceException {
        CakeEntity entity = cakeRepository.save(
                new CakeEntity("Lemon", BigDecimal.valueOf(4), "lemon"));
        CakeDTO dto = cakeService.getCake(entity.getCakeId());

        assertNotNull(dto);
        assertEquals("Lemon", dto.getName());
    }

    /**
     * Test: Attempt to retrieve a non-existent cake by ID and expect an exception.
     */
    @Test
    @DisplayName("getCake – returns empty when absent")
    void getCake_absent() {
        assertThrows(CakeServiceException.class,
                () -> cakeService.getCake(777));
    }

    /**
     * Test: Save multiple cakes and verify the service returns a correctly mapped list.
     */
    @Test
    @DisplayName("getAllCakes – maps list correctly")
    void getAllCakes() throws CakeServiceException {
        cakeRepository.save(new CakeEntity("A", BigDecimal.ONE, "A"));
        cakeRepository.save(new CakeEntity("B", BigDecimal.TEN, "B"));

        List<CakeDTO> list = cakeService.getAllCakes();

        assertEquals(2, list.size());
    }

    /**
     * Test configuration class that provides a {@link MapperFacade} bean for the test context.
     * Uses Orika to map between entities and DTOs.
     */
    @Configuration
    static class TestConfig {
        @Bean
        public MapperFacade mapperFacade() {
            return new DefaultMapperFactory.Builder().build().getMapperFacade();
        }
    }
}
