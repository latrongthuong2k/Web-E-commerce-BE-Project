package com.ecommerce.myapp.dto.product;

import com.ecommerce.myapp.model.product.Tags;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link Tags}
 */
public record TagsDto(@NotNull Integer id, @NotNull String tagName) implements Serializable {
}