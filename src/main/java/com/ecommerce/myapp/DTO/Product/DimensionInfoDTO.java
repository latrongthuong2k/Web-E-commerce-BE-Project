package com.ecommerce.myapp.DTO.Product;

import java.math.BigDecimal;

public record DimensionInfoDTO(
        BigDecimal weight,
        BigDecimal length,
        BigDecimal width,
        BigDecimal height) {
}
