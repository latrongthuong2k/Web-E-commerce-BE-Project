package com.ecommerce.myapp.dto.bill;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ReqCreateSales(
       @NotNull Integer quantity,
       @NotNull BigDecimal salePrice,
       @NotNull Integer productId
) {
}
