package com.ecommerce.myapp.dto.product;

import java.math.BigDecimal;

public record DimensionInfoDTO(
        BigDecimal weight,
        BigDecimal length,
        BigDecimal width,
        BigDecimal height) {
}
