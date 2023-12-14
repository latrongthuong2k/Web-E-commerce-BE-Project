package com.ecommerce.myapp.dto.product;

import com.ecommerce.myapp.model.product.Colors;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link Colors}
 */
public record ColorsDto(@NotNull Integer id, @NotNull String colorName) implements Serializable {
}