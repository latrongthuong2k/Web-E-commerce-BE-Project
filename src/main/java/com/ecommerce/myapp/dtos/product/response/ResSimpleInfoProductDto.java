package com.ecommerce.myapp.dtos.product.response;


import com.ecommerce.myapp.model.group.Product;
import com.ecommerce.myapp.s3.S3ObjectCustom;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO for {@link Product}
 */
public record ResSimpleInfoProductDto(
        Long productId,
        String productName,
        BigDecimal unitPrice,
        Set<S3ObjectCustom> s3ObjectCustoms
) implements Serializable {
}