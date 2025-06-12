package com.waracle.cakemgr.persistance.repository;

import com.waracle.cakemgr.persistance.entity.CakeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing persistence operations on {@link CakeEntity}.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations,
 * pagination, and sorting capabilities for Cake entities.
 * </p>
 *
 * <p>
 * This interface enables Spring Data JPA to generate implementation code automatically.
 * </p>
 *
 * @author Omkar Tamboli
 */
@Repository
public interface CakeRepository extends JpaRepository<CakeEntity, Integer> {
}
