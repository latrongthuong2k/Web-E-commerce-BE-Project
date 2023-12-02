package com.ecommerce.myapp.DTO.Product;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.ecommerce.myapp.Entity.ProductConnectEntites.Tags}
 */
public record TagsDto(@NotNull Integer id, @NotNull String tagName) implements Serializable {
}