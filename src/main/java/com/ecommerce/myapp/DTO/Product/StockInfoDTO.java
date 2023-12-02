package com.ecommerce.myapp.DTO.Product;

import jakarta.validation.constraints.Min;

public record StockInfoDTO(@Min(0) Integer stockQuantity) { }
