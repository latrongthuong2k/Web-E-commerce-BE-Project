package com.ecommerce.myapp.DTO.Product;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.ecommerce.myapp.Entity.ProductConnectEntites.Sizes}
 */
public record SizesDto(@NotNull Integer id, @NotNull String sizeValue) implements Serializable {
}