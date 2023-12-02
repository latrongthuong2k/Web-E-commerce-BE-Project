package com.ecommerce.myapp.DTO.Product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PricingInfoDTO(@Min(0) BigDecimal discountPrice, @NotNull LocalDateTime expirationDate) {
}
