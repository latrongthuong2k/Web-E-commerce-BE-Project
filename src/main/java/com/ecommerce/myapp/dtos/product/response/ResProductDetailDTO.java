package com.ecommerce.myapp.dtos.product.response;

import com.ecommerce.myapp.model.group.Product;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link Product}
 */
@Setter
@Getter
public class ResProductDetailDTO implements Serializable {
    Integer id;
    String productName;
    BigDecimal price;
    Integer stockQuantity;
    Integer categoryId;
    Integer supplierId;
    String description;
}