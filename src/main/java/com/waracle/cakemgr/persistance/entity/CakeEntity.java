package com.waracle.cakemgr.persistance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entity class representing a Cake record in the database.
 * Maps to the "Cake" table with unique constraint on the ID column.
 *
 * @author Omkar Tamboli
 */
@Entity
@Table(name = "Cake", uniqueConstraints = {@UniqueConstraint(columnNames = "ID")})
public class CakeEntity {

    /**
     * The primary key identifier for the cake.
     * Auto-generated and unique.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer cakeId;

    /**
     * The name of the cake.
     * Cannot be null and has a maximum length of 100 characters.
     */
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    /**
     * The price of the cake.
     * Cannot be null.
     */
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    /**
     * Optional description of the cake.
     * Can be null, with a maximum length of 300 characters.
     */
    @Column(name = "DESCRIPTION", length = 300)
    private String description;

    /**
     * Default no-argument constructor required by JPA.
     */
    public CakeEntity() {
    }

    /**
     * Constructs a new CakeEntity with the specified name, price, and description.
     *
     * @param name        the name of the cake
     * @param price       the price of the cake
     * @param description the description of the cake
     */
    public CakeEntity(String name, BigDecimal price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    /**
     * Returns the cake ID.
     *
     * @return the cakeId
     */
    public Integer getCakeId() {
        return cakeId;
    }

    /**
     * Sets the cake ID.
     *
     * @param cakeId the cakeId to set
     */
    public void setCakeId(Integer cakeId) {
        this.cakeId = cakeId;
    }

    /**
     * Returns the cake name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the cake name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the cake price.
     *
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the cake price.
     *
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Returns the cake description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the cake description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
