package com.ecommerce.myapp.dto.product;

import jakarta.validation.constraints.Min;

public record StockInfoDTO(@Min(0) Integer stockQuantity) { }
