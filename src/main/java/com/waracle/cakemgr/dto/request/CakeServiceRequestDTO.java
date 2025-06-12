package com.waracle.cakemgr.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.waracle.cakemgr.security.XssStringDeserializer;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing a Cake Create and Update Request Body.
 * <p>
 * This class is used to transfer cake data between client and server.
 * It supports both JSON and XML serialization/deserialization.
 * </p>
 *
 * <p>
 * This class implements {@link Serializable} to support caching and distributed systems.
 * </p>
 *
 * @author Omkar Tamboli
 */
public class CakeServiceRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Name of the cake.
     */
    @NotBlank(message = "Cake name must not be blank")
    @Size(max = 100, message = "Cake name must be at most 100 characters")
    @JsonDeserialize(using = XssStringDeserializer.class)
    private String name;

    /**
     * Price of the cake.
     */
    @NotNull(message = "Cake price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cake price must be greater than zero")
    private BigDecimal price;

    /**
     * Description of the cake.
     */
    @Size(max = 300, message = "Cake description must be at most 300 characters")
    @JsonDeserialize(using = XssStringDeserializer.class)
    private String description;

    /**
     * Default no-args constructor required for serialization and frameworks.
     */
    public CakeServiceRequestDTO() {
    }

    /**
     * Gets the name of the cake.
     *
     * @return the name of the cake
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the cake.
     *
     * @param name the new name of the cake
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the cake.
     *
     * @return the price of the cake
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the price of the cake.
     *
     * @param price the new price of the cake
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets the description of the cake.
     *
     * @return the description of the cake
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the cake.
     *
     * @param description the new description of the cake
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public CakeServiceRequestDTO(String name, BigDecimal price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    /**
     * Returns a string representation of the {@code CakeDTO}.
     *
     * @return string containing the cake's details
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CakeServiceRequestDTO{");
        sb.append(", name='").append(name).append('\'');
        sb.append(", price=").append(price);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
