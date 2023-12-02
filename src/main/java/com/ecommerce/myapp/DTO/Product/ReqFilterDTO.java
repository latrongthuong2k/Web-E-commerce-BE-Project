package com.ecommerce.myapp.DTO.Product;

import com.ecommerce.myapp.Entity.ProductConnectEntites.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO for {@link Product}
 */
public record ReqFilterDTO(
        @NotNull Integer productId,
        String productName,
        BigDecimal price,
        Integer clientTypeId,
        Set<Colors> colors,
        Set<Sizes> sizes,
        Set<Tags> tags,
        Set<ProductImage> images
) implements Serializable {
}