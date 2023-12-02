package com.ecommerce.myapp.DTO;

import jakarta.validation.constraints.NotNull;

public record UserWithProductDTO(
        @NotNull Integer userId,
        @NotNull Integer productId,
        @NotNull Integer quantity
){
}
