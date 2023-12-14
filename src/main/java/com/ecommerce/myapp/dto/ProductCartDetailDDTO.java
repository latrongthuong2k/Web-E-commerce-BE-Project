package com.ecommerce.myapp.dto;

import com.ecommerce.myapp.model.client.ProductCartDetail;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link ProductCartDetail}
 */
public record ProductCartDetailDDTO(
        @NotNull Integer cart,
        @NotNull Integer product,
        @NotNull Integer quantity,
        String comment
){
}