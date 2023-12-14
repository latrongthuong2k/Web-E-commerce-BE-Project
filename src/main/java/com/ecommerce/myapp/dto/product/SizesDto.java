package com.ecommerce.myapp.dto.product;

import com.ecommerce.myapp.model.product.Sizes;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link Sizes}
 */
public record SizesDto(@NotNull Integer id, @NotNull String sizeValue) implements Serializable {
}