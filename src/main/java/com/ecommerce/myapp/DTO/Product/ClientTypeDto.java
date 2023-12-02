package com.ecommerce.myapp.DTO.Product;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.ecommerce.myapp.Entity.ProductConnectEntites.ClientType}
 */
public record ClientTypeDto(@NotNull Integer id, @NotNull String typeName) implements Serializable {
}