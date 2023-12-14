package com.ecommerce.myapp.dto.product;


import com.ecommerce.myapp.model.product.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link Product}
 */
public record ResViewProductDTO(
        @Size(max = 50) @NotBlank String productName,
        String description,
        @Min(0) BigDecimal price,
        @NotNull List<ClientType> clientType,
        @NotNull List<Colors> colors,
        @NotNull List<Sizes> sizes,
        @NotNull List<Tags> tags,
        @NotNull List<ProductImage> images
) implements Serializable {
}