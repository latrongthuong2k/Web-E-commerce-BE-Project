package com.ecommerce.myapp.dto.product;

import com.ecommerce.myapp.model.product.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link Product}
 */
public record ResProductDetailDTO(
        @NotNull Integer productId,
        @NotNull @Size(max = 100) String productName,
        @NotNull BigDecimal price,
        @NotNull Integer stockQuantity,
        @NotNull Integer category,
        @NotNull List<ColorsDto> colors,
        @NotNull List<SizesDto> sizes,
        @NotNull List<TagsDto> tags,
        @NotNull List<ClientTypeDto> clientTypes,
        @NotNull Integer supplier,
        @Size(max = 200) String description
) implements Serializable {
}