package com.ecommerce.myapp.DTO.Product;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.ecommerce.myapp.Entity.ProductConnectEntites.Colors}
 */
public record ColorsDto(@NotNull Integer id, @NotNull String colorName) implements Serializable {
}