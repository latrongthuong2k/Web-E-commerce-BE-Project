package com.ecommerce.myapp.dto.product;

import com.ecommerce.myapp.model.product.ClientType;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link ClientType}
 */
public record ClientTypeDto(@NotNull Integer id, @NotNull String typeName) implements Serializable {
}