package com.ecommerce.myapp.dto.product;

import com.ecommerce.myapp.model.product.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link Product}
 */
public record ProductBasicInfoDTO(
        @NotNull Integer id,

        @Size(max = 50) @NotBlank String productName,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        @NotNull Integer quantity,
        @Min(0) BigDecimal price,
        @NotNull Integer status
) implements Serializable {
}